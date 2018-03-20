package com.wnlbs.checkposition.dataobject;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: huangxin
 * @Date: Created in 上午10:44 2018/3/20
 * @Description:
 */
@Data
public class GaoDeJSONSuggestion implements Serializable {
    private static final long serialVersionUID = -6156637136449037004L;
    private String[] keywords;

    private String[] cities;

}
