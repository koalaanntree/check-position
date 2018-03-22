package com.wnlbs.checkposition;

import com.alibaba.fastjson.JSON;
import com.wnlbs.checkposition.dataobject.GaoDeJSONFormatBean;
import com.wnlbs.checkposition.enums.ProvincesEnum;
import com.wnlbs.checkposition.rabbit.message.SavePositionMessage;
import com.wnlbs.checkposition.request.PositionRequest;
import com.wnlbs.checkposition.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Author: huangxin
 * @Date: Created in 下午2:05 2018/3/21
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ScriptForInitDB {

    //    获取上下文对象
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private AmqpTemplate amqpTemplate;

    //    拿一个模拟http发射器
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void initDB() throws Exception{

        ProvincesEnum[] provincesEnums = ProvincesEnum.values();

        for (ProvincesEnum provincesEnum:provincesEnums){
            PositionRequest positionRequest = new PositionRequest();
            positionRequest.setFilter(provincesEnum.getFilter());
            positionRequest.setKeywords(provincesEnum.getProvinceName());
            positionRequest.setExtensions("all");
            positionRequest.setSubdistrict(1);
            String jsonString = JSON.toJSONString(positionRequest);
            String result = mockMvc.perform(post("/position/initdb")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(jsonString))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
            log.info(result);
        }
    }

    @Test
    public void initDBRemote() throws Exception{

        ProvincesEnum[] provincesEnums = ProvincesEnum.values();

        for (ProvincesEnum provincesEnum:provincesEnums){
            //发送Post请求
            PositionRequest positionRequest = new PositionRequest();
            positionRequest.setFilter(provincesEnum.getFilter());
            positionRequest.setKeywords(provincesEnum.getProvinceName());
            positionRequest.setExtensions("all");
            positionRequest.setSubdistrict(1);
            String jsonString = JSON.toJSONString(positionRequest);

            //发送Post请求
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://http://10.42.240.81:9968/");


            String result = mockMvc.perform(post("/position/initdb")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(jsonString))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
            log.info(result+"");
        }
    }

}
