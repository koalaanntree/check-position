package com.wnlbs.checkposition.controller;

import com.wnlbs.checkposition.dataobject.GaoDeJSONFormatBean;
import com.wnlbs.checkposition.rabbit.message.SavePositionMessage;
import com.wnlbs.checkposition.request.PositionRequest;
import com.wnlbs.checkposition.service.PositionService;
import com.wnlbs.checkposition.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: huangxin
 * @Date: Created in 上午9:33 2018/3/20
 * @Description: 定位服务
 */
@RestController
@Slf4j
@RequestMapping("/position")
public class PositionController {
    /**
     * GET请求URL路径
     */
    private static final String URL = "http://restapi.amap.com/v3/config/district?" +
            "extensions=%s&keywords=%s&subdistrict=%s&key=%s";

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 获取边界线
     */
    @PostMapping("/initdb")
    @ResponseBody
    public String getPolyLine(@RequestBody PositionRequest positionRequest) throws Exception {
        //拿到请求对象
        log.info("positionRequest:" + positionRequest.toString());
        //拿到请求路径
        String url = httpUtils.getRealUrl(positionRequest);
        //第一步，进入正题，拿到原文的响应对象，并且转化为项目中自己的bean
        GaoDeJSONFormatBean bean = httpUtils.queryPolyLine(url);
        //发送消息到消息队列，利用队列存储数据
        SavePositionMessage savePositionMessage = new SavePositionMessage(bean, 0);
        amqpTemplate.convertAndSend("check.position", "DBData", savePositionMessage);
        //TODO 第三步 持久化第二步中的子类对象进入数据库 单独写方法
        //TODO 第四步 写入缓存
        log.info("bean:" + bean.toString());
        return "OK";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }


}
