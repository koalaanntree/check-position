package com.wnlbs.checkposition.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: huangxin
 * @Date: Created in 下午1:05 2018/3/22
 * @Description:
 */
@Configuration
@ConfigurationProperties(prefix = "position.save")
@Data
public class PositionProperties {
    private String path;
}
