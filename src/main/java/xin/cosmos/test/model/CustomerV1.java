package xin.cosmos.test.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class CustomerV1 {
    @JSONField(name = "Index")
    @ExcelProperty(value = "Index")
    private String c0;
    @JSONField(name = "序号")
    @ExcelProperty(value = "序号")
    private String c1;
    @JSONField(name = "客户号")
    @ExcelProperty(value = "客户号")
    private String c2;
    @JSONField(name = "冻结机关")
    @ExcelProperty(value = "查冻扣单位")
    private String c3;
    @JSONField(name = "类型")
    @ExcelProperty(value = "类型")
    private String c4;
    @JSONField(name = "客户名称")
    @ExcelProperty(value = "客户名称")
    private String c5;
    @JSONField(name = "公私标识")
    @ExcelProperty(value = "公私标识")
    private String c6;
    @JSONField(name = "异常账号")
    @ExcelProperty(value = "异常账号")
    private String c7;
    @JSONField(name = "查冻扣时间")
    @ExcelProperty(value = "查冻扣日期")
    private String c9;

    @JSONField(name = "查冻扣原由")
    @ExcelProperty(value = "查冻扣原由")
    private String c10;

    @JSONField(name = "账户状态")
    @ExcelProperty(value = "账户状态")
    private String c11;

    @JSONField(name = "账户名称")
    @ExcelProperty(value = "账户名称")
    private String c13;

    @JSONField(name = "客户身份证号码")
    @ExcelProperty(value = "身份证号码")
    private String c14;

    @JSONField(name = "开户机构")
    @ExcelProperty(value = "开户机构")
    private String c15;

    @JSONField(name = "查询日期")
    @ExcelProperty(value = "查询日期")
    private String c16;

    @JSONField(name = "省市区")
    @ExcelProperty(value = "省市区")
    private String c19;

    @JSONField(name = "城市名")
    @ExcelProperty(value = "城市名")
    private String c20;

    @JSONField(name = "备注信息")
    @ExcelProperty(value = "备注信息")
    private String c21;

    @JSONField(name = "职业")
    @ExcelProperty(value = "职业")
    private String c22;

    @JSONField(name = "行业")
    @ExcelProperty(value = "行业")
    private String c23;

    @JSONField(name = "电话")
    @ExcelProperty(value = "电话")
    private String c24;

    @JSONField(name = "月收入")
    @ExcelProperty(value = "月收入")
    private String c25;

    @JSONField(name = "年薪")
    @ExcelProperty(value = "年薪")
    private String c26;

    @JSONField(name = "工作单位")
    @ExcelProperty(value = "工作单位")
    private String c27;

    @JSONField(name = "证件地址")
    @ExcelProperty(value = "证件地址")
    private String c28;

    @JSONField(name = "经常居住地址")
    @ExcelProperty(value = "经常居住地址")
    private String c29;

    @JSONField(name = "是否开通网银")
    @ExcelProperty(value = "是否开通网银")
    private String c30;

    @JSONField(name = "是否开通手机银行")
    @ExcelProperty(value = "是否开通手机银行")
    private String c31;

    @JSONField(name = "账号")
    @ExcelProperty(value = "账号")
    private String c32;

    @JSONField(name = "每个状态")
    @ExcelProperty(value = "每个状态")
    private String c33;

    @JSONField(name = "每个账户开户时间")
    @ExcelProperty(value = "每个账户开户时间")
    private String c34;

    @JSONField(name = "是否有注册公司")
    @ExcelProperty(value = "是否有注册公司")
    private String c35;

    @JSONField(name = "公司名称")
    @ExcelProperty(value = "公司名称")
    private String c36;

    @JSONField(name = "统一社会信用代码证")
    @ExcelProperty(value = "统一社会信用代码证")
    private String c37;

    @JSONField(name = "开立时间")
    @ExcelProperty(value = "开立时间")
    private String c38;

    @JSONField(name = "是否有司法查冻扣")
    @ExcelProperty(value = "是否有司法查冻扣")
    private String c39;

    @JSONField(name = "查冻扣的类型")
    @ExcelProperty(value = "查冻扣的类型")
    private String c40;

    @JSONField(name = "查冻扣的文书号")
    @ExcelProperty(value = "查冻扣的文书号")
    private String c41;

    @JSONField(name = "客户是否有购买定期或者理财产品")
    @ExcelProperty(value = "客户是否有购买定期或者理财产品")
    private String c42;

    @JSONField(name = "产品名称")
    @ExcelProperty(value = "产品名称")
    private String c43;

    @JSONField(name = "产品金额")
    @ExcelProperty(value = "产品金额")
    private String c44;

    @JSONField(name = "产品购买金额")
    @ExcelProperty(value = "产品购买金额")
    private String c45;

    @JSONField(name = "检查期限账户交易借方笔数")
    @ExcelProperty(value = "检查期限账户交易借方笔数")
    private String c46;

    @JSONField(name = "检查期限账户交易借方金额")
    @ExcelProperty(value = "检查期限账户交易借方金额")
    private String c47;
    @JSONField(name = "检查期限账户交易贷方笔数")
    @ExcelProperty(value = "检查期限账户交易贷方笔数")
    private String c48;
    @JSONField(name = "检查期限账户交易贷方金额")
    @ExcelProperty(value = "检查期限账户交易贷方金额")
    private String c49;
    @JSONField(name = "检查期限借方主要交易对手")
    @ExcelProperty(value = "检查期限借方主要交易对手")
    private String c50;
    @JSONField(name = "检查期限贷方主要交易对手")
    @ExcelProperty(value = "检查期限贷方主要交易对手")
    private String c51;
    @JSONField(name = "检查期限摘要")
    @ExcelProperty(value = "检查期限摘要")
    private String c52;
    @JSONField(name = "账户交易借方笔数")
    @ExcelProperty(value = "账户交易借方笔数")
    private String c53;
    @JSONField(name = "账户交易借方金额")
    @ExcelProperty(value = "账户交易借方金额")
    private String c54;

    @JSONField(name = "账户交易贷方笔数")
    @ExcelProperty(value = "账户交易贷方笔数")
    private String c55;

    @JSONField(name = "账户交易贷方金额")
    @ExcelProperty(value = "账户交易贷方金额")
    private String c56;

    @JSONField(name = "借方主要交易对手")
    @ExcelProperty(value = "借方主要交易对手")
    private String c57;

    @JSONField(name = "贷方主要交易对手")
    @ExcelProperty(value = "贷方主要交易对手")
    private String c58;

    @JSONField(name = "文书编号")
    @ExcelProperty(value = "文书编号")
    private String c62;

    @JSONField(name = "摘要")
    @ExcelProperty(value = "摘要")
    private String c59;

    @JSONField(name = "案件")
    @ExcelProperty(value = "案件")
    private String c63;


    //TODO EasyExcel Bug @ExcelProperty注解中间如果没有标注得有的属性，该属性的属性即便有@ExcelProperty标注也不生效
    @JSONField(name = "最早账户交易时间")
    @ExcelProperty(value = "最早账户交易时间")
    private String c60;
    @JSONField(name = "最晚账户交易时间")
    @ExcelProperty(value = "最晚账户交易时间")
    private String c61;
}
