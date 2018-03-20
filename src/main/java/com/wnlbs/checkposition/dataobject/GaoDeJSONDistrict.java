package com.wnlbs.checkposition.dataobject;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: huangxin
 * @Date: Created in 上午10:47 2018/3/20
 * @Description:
 */
@Data
public class GaoDeJSONDistrict implements Serializable{
    private static final long serialVersionUID = 8967819799287336818L;
    private String citycode;
    private String adcode;
    private String name;
    private String polyline;
    private String center;
    private String level;
    private GaoDeJSONDistrict[] districts;
}
