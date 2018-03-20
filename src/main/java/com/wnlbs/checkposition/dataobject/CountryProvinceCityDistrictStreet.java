package com.wnlbs.checkposition.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Author: huangxin
 * @Date: Created in 上午11:18 2018/3/20
 * @Description: 国家、省份、城市、区县、街道 5级详细信息表
 */
@Data
@Entity
public class CountryProvinceCityDistrictStreet implements Serializable{
    private static final long serialVersionUID = -3124812770643318890L;

    /**
     * 表中主键id
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 上级行政区id
     */
    private Integer parentid;

    /**
     * 所属城市编码
     */
    private String citycode;

    /**
     * 所属位置邮编
     */
    private String adcode;

    /**
     * 名称全称
     */
    private String name;

    /**
     * 边界线存储地址
     */
    private String polyline;

    /**
     * 中心点坐标
     */
    private String center;

    /**
     * 行政级别
     */
    private String level;

}
