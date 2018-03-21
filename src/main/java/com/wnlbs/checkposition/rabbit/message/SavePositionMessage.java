package com.wnlbs.checkposition.rabbit.message;

import com.wnlbs.checkposition.dataobject.GaoDeJSONFormatBean;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: huangxin
 * @Date: Created in 下午1:30 2018/3/21
 * @Description:
 */
@Data
public class SavePositionMessage implements Serializable{

    private static final long serialVersionUID = -8292024800970986481L;

    private GaoDeJSONFormatBean gaoDeJSONFormatBean;

    private Integer parentId;

    public SavePositionMessage(GaoDeJSONFormatBean gaoDeJSONFormatBean, Integer parentId) {
        this.gaoDeJSONFormatBean = gaoDeJSONFormatBean;
        this.parentId = parentId;
    }
}
