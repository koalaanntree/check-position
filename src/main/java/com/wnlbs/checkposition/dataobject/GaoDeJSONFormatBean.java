package com.wnlbs.checkposition.dataobject;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: huangxin
 * @Date: Created in 上午10:43 2018/3/20
 * @Description:
 */
@Data
public class GaoDeJSONFormatBean implements Serializable{
    private static final long serialVersionUID = -4383467441543717260L;
    private String status;
    private String info;
    private String infocode;
    private String count;
    private GaoDeJSONSuggestion suggestion;
    private GaoDeJSONDistrict[] districts;

}
