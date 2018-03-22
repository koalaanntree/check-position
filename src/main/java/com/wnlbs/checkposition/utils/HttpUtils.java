package com.wnlbs.checkposition.utils;

import com.alibaba.fastjson.JSON;
import com.wnlbs.checkposition.dataobject.GaoDeJSONFormatBean;
import com.wnlbs.checkposition.request.PositionRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: huangxin
 * @Date: Created in 下午4:05 2018/3/20
 * @Description:
 */
@Component
@Slf4j
public class HttpUtils {
    /**
     * GET请求URL路径
     */
    private static final String URL = "http://restapi.amap.com/v3/config/district?" +
            "extensions=%s&keywords=%s&subdistrict=%s&key=%s&filter=%s";

    /**
     * apache httpClient
     */
    @Autowired
    HttpClient httpClient;

    /**
     * 获取边界线
     */
    public GaoDeJSONFormatBean queryPolyLine(String url) throws Exception{
        //实例化get请求
        HttpGet httpGet = new HttpGet(url);
        //查看拼接的uri
        log.info("request url:" + httpGet.getURI());
        //拿到响应对象实例
        CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(httpGet);
        //拿到响应内容
        HttpEntity entity = response.getEntity();
        String entityString = EntityUtils.toString(entity);
        log.info("response content:" + entityString);
        //第一步，进入正题，拿到原文的响应对象，并且转化为项目中自己的bean
        GaoDeJSONFormatBean bean = JSON.parseObject(entityString, GaoDeJSONFormatBean.class);
        return bean;
    }

    /**
     * 封装url
     */
    public String getRealUrl(PositionRequest positionRequest) {
        String url = String.format(URL, positionRequest.getExtensions(), positionRequest.getKeywords()
                , positionRequest.getSubdistrict(), positionRequest.getKEY(),positionRequest.getFilter());
        return url;
    }
}
