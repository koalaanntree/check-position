package com.wnlbs.checkposition.service;

import com.wnlbs.checkposition.rabbit.message.SavePositionMessage;

/**
 * @Author: huangxin
 * @Date: Created in 下午2:16 2018/3/20
 * @Description:
 */
public interface PositionService {

    /**
     * 存入数据库的方法
     * @param savePositionMessage
     * @return
     */
    boolean saveToDB(SavePositionMessage savePositionMessage);

}
