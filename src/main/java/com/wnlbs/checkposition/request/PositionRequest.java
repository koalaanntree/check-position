package com.wnlbs.checkposition.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: huangxin
 * @Date: Created in 上午9:51 2018/3/20
 * @Description: 定位请求发送主体类
 */
@ToString
public class PositionRequest {
    /**
     * 请求秘钥:
     * 不可更改
     */
    @Getter
    private final String KEY = "2a7fef3bb9246b22bdfe7f724a255072";
    /**
     * 子级行政区:
     * 可选值：0、1、2、3等数字，并以此类推
     * <p>
     * 0：不返回下级行政区；
     * <p>
     * 1：返回下一级行政区；
     * <p>
     * 2：返回下两级行政区；
     * <p>
     * 3：返回下三级行政区；
     */
    @Getter
    @Setter
    private Integer subdistrict;
    /**
     * 返回结果控制:
     * base:不返回行政区边界坐标点；
     * <p>
     * all:只返回当前查询district的边界值，不返回子节点的边界值；
     * <p>
     * 目前不能返回乡镇/街道级别的边界值
     */
    @Getter
    @Setter
    private String extensions = "all";
    /**
     * 查询关键字:
     * 规则：只支持单个关键词语搜索关键词支持：行政区名称、citycode、adcode
     * <p>
     * 例如，在subdistrict=2，搜索省份（例如山东），能够显示市（例如济南），区（例如历下区）
     */
    @Getter
    @Setter
    private String keywords;

    /**
     * 过滤关键字filter
     */
    @Getter
    @Setter
    private String filter;
}
