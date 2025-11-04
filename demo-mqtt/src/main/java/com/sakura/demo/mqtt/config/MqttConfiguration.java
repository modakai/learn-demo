package com.sakura.demo.mqtt.config;

import com.sakura.demo.mqtt.domain.MqttProperties;
import jakarta.annotation.Resource;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

@Configuration
public class MqttConfiguration {

    @Resource
    private MqttProperties mqttProperties;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions connectOptions = new MqttConnectOptions();

        connectOptions.setCleanSession(true);
        connectOptions.setUserName(mqttProperties.getUsername());
        connectOptions.setPassword(mqttProperties.getPassword().toCharArray());
        connectOptions.setServerURIs(new String[]{mqttProperties.getUrl()});
        factory.setConnectionOptions(connectOptions);
        return factory;
    }
}
