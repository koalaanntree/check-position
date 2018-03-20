package com.wnlbs.checkposition.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: huangxin
 * @Date: Created in 下午2:52 2018/3/20
 * @Description:
 */
@Configuration
public class HTTPConfig {

    @Bean
    public HttpClient httpClient() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        return httpClient;
    }
}
