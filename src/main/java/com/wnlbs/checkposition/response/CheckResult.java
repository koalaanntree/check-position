package com.wnlbs.checkposition.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: huangxin
 * @Date: Created in 上午10:03 2018/3/22
 * @Description:
 */
@Data
public class CheckResult implements Serializable{
    private static final long serialVersionUID = 5185488785576158683L;
    private String province;
    private String city;
    private String district;
}
