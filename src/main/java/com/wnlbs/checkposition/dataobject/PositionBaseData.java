package com.wnlbs.checkposition.dataobject;

import lombok.Data;

import java.awt.geom.GeneralPath;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * @Author: huangxin
 * @Date: Created in 下午3:17 2018/3/21
 * @Description: 省份
 */
@Data
public class PositionBaseData implements Serializable{
    private static final long serialVersionUID = 3482144033795779116L;
    /**
     * 存入缓存的key
     */
    private String cachedKey;
    /**
     * 地理模型名称
     */
    private String modelName;
    /**
     * 地理模型级别
     */
    private String modelLevel;
    /**
     * 单位地理模型集合
     */
    private Vector<GeneralPath> generalPaths;
    /**
     * 自包含集合,方便迭代
     */
    private List<PositionBaseData> positionBaseDataList;
}
