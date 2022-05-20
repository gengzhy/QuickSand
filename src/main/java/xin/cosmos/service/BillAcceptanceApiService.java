package xin.cosmos.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import xin.cosmos.basic.api.IBillAcceptanceApiService;
import xin.cosmos.basic.api.dict.bill.disclosure.ShowStatus;
import xin.cosmos.basic.api.param.AccInfoListByAcptNameParam;
import xin.cosmos.basic.api.param.FindSettlePageParam;
import xin.cosmos.basic.api.vo.*;
import xin.cosmos.basic.base.RedisService;
import xin.cosmos.basic.constant.Constant;
import xin.cosmos.basic.define.ResultVO;
import xin.cosmos.basic.define.SingleParam;
import xin.cosmos.basic.dict.IDict;
import xin.cosmos.basic.dict.bill.disclosure.BillAcceptanceBusiType;
import xin.cosmos.basic.exception.BusinessException;
import xin.cosmos.basic.util.ObjectsUtil;
import xin.cosmos.dto.BillAcceptanceDisclosureDataExcelDownloadDTO;
import xin.cosmos.dto.BillAcceptanceMetaDataExcelUploadDTO;
import xin.cosmos.entity.BillAcceptanceExcelDownloadParam;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * 票据承兑人信息接口服务类
 *
 * @author geng
 */
@Slf4j
@Service
public class BillAcceptanceApiService {

    private IBillAcceptanceApiService billAcceptanceApiService;
    @Autowired
    private RedisService redisService;

    @Value("sub-bill-acceptance-left-brackets:false")
    private String isSubstrLeftBracket;

    public BillAcceptanceApiService(IBillAcceptanceApiService billAcceptanceApiService) {
        this.billAcceptanceApiService = billAcceptanceApiService;
    }

    /**
     * 根据票据承兑人名称查询票据承兑人信息列表
     *
     * @param acceptName 票据承兑人名称
     * @return
     */
    public ResultVO<AccInfoListByAcptNameVO> findAccInfoListByAcptName(SingleParam<String> acceptName) {
        if (ObjectsUtil.isNull(acceptName) || ObjectsUtil.isNull(acceptName.getKey())) {
            throw new BusinessException("票据承兑人名称为空");
        }
        AccInfoListByAcptNameParam param = new AccInfoListByAcptNameParam();
        param.setBillAcceptanceName(acceptName.getKey());
        param.setRandom(ObjectsUtil.randomNumber(5));
        return ResultVO.success(billAcceptanceApiService.findAccInfoListByAcptName(param));
    }

    /**
     * 票据承兑信用信息披露查询
     *
     * @param param 请求参数
     * @return
     */
    public ResultVO<FindSettlePageVO> findSettlePage(FindSettlePageParam param) {
        param.setCurrent(1);
        param.setSize(100);
        return ResultVO.success(billAcceptanceApiService.findSettlePage(param));
    }

    /**
     * 票据承兑人元数据Excel上传
     * 文件上传（缓存到Redis）
     *
     * @param file
     * @param busiType 业务类型
     */
    @SneakyThrows
    public void uploadBillAcceptanceMetaData(MultipartFile file, String busiType) {
        // 业务类型
        BillAcceptanceBusiType acceptanceBusiType;
        if ((acceptanceBusiType = IDict.findByName(busiType, BillAcceptanceBusiType.class)) == null) {
            throw new RuntimeException("文件下载类型为空");
        }
        List<BillAcceptanceMetaDataExcelUploadDTO> corpEntities = new LinkedList<>();
        EasyExcel.read(file.getInputStream(), BillAcceptanceMetaDataExcelUploadDTO.class,
                new PageReadListener<BillAcceptanceMetaDataExcelUploadDTO>(list -> {
                    list.sort(Comparator.comparing(BillAcceptanceMetaDataExcelUploadDTO::getIndex));
                    corpEntities.addAll(list);
                })).sheet().doRead();
        redisService.setList(Constant.getMetaDataStoreKey(acceptanceBusiType.name()), corpEntities);
    }

    /**
     * 企业票据承兑信用信息披露查询
     * 当数据爬取失败时，不影响已经爬取成功的数据
     * 下次再进行爬取时，若缓存中存在爬取失败的数据标志位，仅会爬取失败的数据
     * 当全部爬取成功后，会删除缓存中爬取失败的标志
     *
     * @return
     */
    public List<BillAcceptanceDisclosureDataExcelDownloadDTO> queryBatchBillAcceptanceDisclosureData(BillAcceptanceExcelDownloadParam param) {
        if (StringUtils.isEmpty(param.getShowMonth())) {
            throw new RuntimeException("披露信息时点日期不能为空");
        }
        // 业务类型
        BillAcceptanceBusiType acceptanceBusiType = param.getBusiType();
        if (acceptanceBusiType == null) {
            throw new RuntimeException("文件下载类型为空");
        }
        String showMonth = (param.getShowMonth().length() > 7) ? param.getShowMonth().substring(0, 7) : param.getShowMonth();
        final AtomicInteger index = new AtomicInteger(0);
        List<BillAcceptanceMetaDataExcelUploadDTO> corpEntities = redisService.getList(Constant.getMetaDataStoreKey(acceptanceBusiType.name()));
        Assert.notEmpty(corpEntities, "请先上传票据承兑人源数据后再进行下载");
        final List<BillAcceptanceDisclosureDataExcelDownloadDTO> data = new LinkedList<>();

        // 企业名称模糊查询处理
        if (Boolean.parseBoolean(isSubstrLeftBracket)) {
            Pattern p = Pattern.compile("[(（]+");
            corpEntities.forEach(e -> {
                String corpName = e.getCorpName();
                if (p.matcher(corpName).find()) {
                    int i = corpName.indexOf("(");
                    corpName = (i != -1) ? corpName.substring(0, i) : corpName.substring(0, corpName.indexOf("（"));
                    e.setCorpName(corpName);
                }
            });
        }

        // 记录上一次处理成功的标志位下标
        String handleSuccessDownloadIndex = Constant.getDownloadOklistIndexKey(acceptanceBusiType.name());

        // 升序排序
        corpEntities.sort(Comparator.comparing(BillAcceptanceMetaDataExcelUploadDTO::getIndex));
        // 异常断点数据处理
        final List<BillAcceptanceMetaDataExcelUploadDTO> finalNewDataSource = new LinkedList<>();
        Integer handleOkIndex = Optional.ofNullable((Integer) redisService.get(handleSuccessDownloadIndex)).orElse(0);
        if (handleOkIndex == 0) {
            finalNewDataSource.addAll(corpEntities);
        } else {
            for (int i = handleOkIndex, end = corpEntities.size(); i < end; i++) {
                finalNewDataSource.add(corpEntities.get(i));
            }
        }

        // 接口数据查询处理
        boolean isFullOk = true;
        String currentHandleCorp = "";
        AccInfoListByAcptNameParam acptNameParam;
        FindSettlePageParam settlePageParam;
        for (BillAcceptanceMetaDataExcelUploadDTO corpEntity : finalNewDataSource) {
            currentHandleCorp = corpEntity.getCorpName();
            // 处理数据，捕获异常，防止成功的数据丢失
            try {
                acptNameParam = new AccInfoListByAcptNameParam();
                acptNameParam.setBillAcceptanceName(currentHandleCorp);
                acptNameParam.setRandom(ObjectsUtil.randomNumber(5));
                AccInfoListByAcptNameVO accInfoListByAcptNameVO = billAcceptanceApiService.findAccInfoListByAcptName(acptNameParam);
                if (accInfoListByAcptNameVO.isSuccess()) {
                    List<AccInfoListByAcptNameVO.Body> list = accInfoListByAcptNameVO.getDataList();
                    for (AccInfoListByAcptNameVO.Body body : list) {
                        settlePageParam = new FindSettlePageParam();
                        settlePageParam.setBillAcceptanceName(body.getBillAcceptanceName());
                        settlePageParam.setBillAcceptanceAccountCode(body.getBillAcceptanceAccountCode());
                        settlePageParam.setShowMonth(showMonth);
                        FindSettlePageVO settlePageVO = billAcceptanceApiService.findSettlePage(settlePageParam);
                        wrapper(settlePageVO, data, index, showMonth);
                    }
                }
                ++handleOkIndex;
            } catch (Exception e) {
                redisService.set(handleSuccessDownloadIndex, handleOkIndex);
                log.error("处理到第几【{}】条数据【{}】，接口处理失败：{}", handleOkIndex, currentHandleCorp, e.getMessage());
                isFullOk = false;
                break;
            }
        }

        // 成功处理完成后清除标记位
        if (isFullOk) {
            redisService.delete(handleSuccessDownloadIndex);
        }
        return data;
    }

    /**
     * 构建响应数据
     *
     * @param settlePageVO
     * @param data
     * @param index
     * @param showMonth
     */
    private void wrapper(FindSettlePageVO settlePageVO, List<BillAcceptanceDisclosureDataExcelDownloadDTO> data, AtomicInteger index, String showMonth) {
        if (settlePageVO == null) {
            return;
        }
        FindSettlePageVOBody body = settlePageVO.getData();
        if (body == null) {
            return;
        }
        FindSettlePageVOBaseInfo baseInfo = body.getBaseInfo();
        List<FindSettlePageVODetailInfo.DetailInfoRecord> records = body.getDetailInfo().getRecords();
        BillAcceptanceDisclosureDataExcelDownloadDTO vo;
        for (FindSettlePageVODetailInfo.DetailInfoRecord r : records) {
            vo = new BillAcceptanceDisclosureDataExcelDownloadDTO();
            vo.setIndex(index.addAndGet(1));
            vo.setShowMonth(showMonth);
            vo.setAcptName(baseInfo.getEntName());
            vo.setOrgType(baseInfo.getOrgType().getDesc());
            vo.setCreditCode(baseInfo.getCreditCode());
            vo.setRegisterDate(baseInfo.getRegisterDate());
            vo.setShowStatus(r.getShowStatus());
            vo.setDimAcptBranchName(r.getDimAcptBranchName());
            if (ShowStatus.NO_SHOW.equals(vo.getShowStatus())) {// 未披露
                String msg = "未披露";
                vo.setAcptAmount(msg);
                vo.setAcptOver(msg);
                vo.setTotalOverdueAmount(msg);
                vo.setOverdueOver(msg);
            } else {
                vo.setAcptAmount(r.getAcptAmount());
                vo.setAcptOver(r.getAcptOver());
                vo.setTotalOverdueAmount(r.getTotalOverdueAmount());
                vo.setOverdueOver(r.getOverdueOver());
            }
            vo.setShowDate(r.getShowDate());
            vo.setRelDate(r.getRelDate());
            vo.setRemark(r.getRemark());
            vo.setEntRemark(r.getEntRemark());
            data.add(vo);
        }
    }
}