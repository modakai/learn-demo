package com.sakura.demo.mqtt.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.mqtt")
public class MqttProperties {

    private String username;
    private String password;
    private String url;
    private String subClientId;
    private String subTopic;
    private String pubClientId;

}
