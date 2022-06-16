package xin.cosmos.report.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.cosmos.basic.base.DBService;
import xin.cosmos.report.dao.DepositIssuanceInterBankDao;
import xin.cosmos.report.entity.DepositIssuanceInterBank;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 金融报表服务类
 */
@Slf4j
@Service
public class FinancialReportService {
    @Autowired
    private DepositIssuanceInterBankDao depositIssuanceInterBankDao;
    @Autowired
    private DBService dbService;

    @Transactional
    public List<DepositIssuanceInterBank> saveAllDepositIssuanceInterBank(List<DepositIssuanceInterBank> dataList) {
        dbService.clearTable("fa_report", "fr_deposit_issuance_inter_bank");
        return depositIssuanceInterBankDao.saveAll(dataList);
    }
}
