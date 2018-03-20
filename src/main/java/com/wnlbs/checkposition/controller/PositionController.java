package com.wnlbs.checkposition.controller;

import com.alibaba.fastjson.JSON;
import com.wnlbs.checkposition.converter.ConverteGaoDeJSONDistrictToCountryProvinceCityDistrictStreet;
import com.wnlbs.checkposition.dataobject.CountryProvinceCityDistrictStreet;
import com.wnlbs.checkposition.dataobject.GaoDeJSONFormatBean;
import com.wnlbs.checkposition.request.PositionRequest;
import com.wnlbs.checkposition.service.PositionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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

    /**
     * 拿到数据服务
     */
    @Autowired
    private PositionService positionService;

    /**
     * 获取边界线
     */
    @PostMapping("/polyline")
    @ResponseBody
    public String getPolyLine(@RequestBody PositionRequest positionRequest) throws Exception {
        //拿到请求对象
        log.info("positionRequest:" + positionRequest.toString());
        //实例化httpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = String.format(URL, positionRequest.getExtensions(), positionRequest.getKeywords()
                , positionRequest.getSubdistrict(), positionRequest.getKEY());
        //实例化get请求
        HttpGet httpGet = new HttpGet(url);
        //查看拼接的uri
        log.info("request url:" + httpGet.getURI());
        //拿到响应对象实例
        CloseableHttpResponse response = httpClient.execute(httpGet);
        //拿到响应内容
        HttpEntity entity = response.getEntity();
        String entityString = EntityUtils.toString(entity);
        log.info("response content:" + entityString);
        //第一步，进入正题，拿到原文的响应对象，并且转化为项目中自己的bean
        GaoDeJSONFormatBean bean = JSON.parseObject(entityString, GaoDeJSONFormatBean.class);
        //TODO 第二步 持久化主类对象进入数据库 单独写方法
        CountryProvinceCityDistrictStreet cityDistrictStreet =
                ConverteGaoDeJSONDistrictToCountryProvinceCityDistrictStreet.convert(bean.getDistricts()[0], true, -1);
        positionService.saveToDB(cityDistrictStreet);
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
