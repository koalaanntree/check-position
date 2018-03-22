package com.wnlbs.checkposition.enums;

import lombok.Getter;

/**
 * @Author: huangxin
 * @Date: Created in 下午1:43 2018/3/21
 * @Description:
 */
@Getter
public enum ProvincesEnum {

    BEIJINGSHI("北京市", "110000"),
    GUIZHOUSHENG("贵州省", "520000"),
    AOMEN("澳门特别行政区", "820000"),
    FUJIANSHENG("福建省", "350000"),
    CHONGQINGSHI("重庆市", "500000"),
    HUBEISHENG("湖北省", "420000"),
//    HAINANSHENG("海南省", "460000"),
//    GUANGDONGSHENG("广东省", "440000"),
//    GUANGXISHENG("广西壮族自治区", "450000"),
//    SHANXISHENG("山西省", "140000"),
//    GANSUSHENG("甘肃省", "620000"),
//    TAIWANSHENG("台湾省", "710000"),
//    JIANGSUSHENG("江苏省", "320000"),
//    HENANSHENG("河南省", "410000"),
//    HEBEISHENG("河北省", "130000"),
//    HUNANSHENG("湖南省", "430000"),
//    ANHUISHENG("安徽省", "340000"),
//    SHANGHAISHI("上海市", "310000"),
//    HEILONGJIANGSHENG("黑龙江省", "230000"),
//    QINGHAISHENG("青海省", "630000"),
//    JIANGXISHENG("江西省", "360000"),
//    NEIMENGGU("内蒙古自治区", "150000"),
//    LIAONINGSHENG("辽宁省", "210000"),
//    NINGXIASHENG("宁夏回族自治区", "640000"),
//    JILINSHENG("吉林省", "220000"),
//    SHANDONGSHENG("山东省", "370000"),
//    SHANXIISHENG("陕西省", "610000"),
//    XINJIANG("新疆维吾尔自治区", "650000"),
//    TIANJINSHI("天津市", "120000"),
//    SICHUANSHENG("四川省", "510000"),
//    XIANGGANG("香港特别行政区", "810000"),
//    XIZANG("西藏自治区", "540000"),
//    YUNNAN("云南省", "530000"),
//    ZHEJIANG("浙江省", "330000"),
    ;
    private String provinceName;

    private String filter;

    ProvincesEnum(String provinceName, String filter) {
        this.provinceName = provinceName;
        this.filter = filter;
    }
}
