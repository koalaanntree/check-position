package com.wnlbs.checkposition.rabbit;

import com.wnlbs.checkposition.rabbit.message.SavePositionMessage;
import com.wnlbs.checkposition.service.PositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: huangxin
 * @Date: Created in 下午1:11 2018/3/21
 * @Description:
 */
@Slf4j
@Component
public class PositionMessageReceiver {

    @Autowired
    private PositionService positionService;

    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange("check.position"),
            key = "DBData",
            value = @Queue("position.data.save")
    ))
    public void processSavePosition(SavePositionMessage savePositionMessage) {
        log.info("queue received message");
        positionService.saveToDB(savePositionMessage);
        log.info("queue done");
    }


}
