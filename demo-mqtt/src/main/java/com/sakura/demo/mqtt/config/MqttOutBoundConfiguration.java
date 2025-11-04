package com.sakura.demo.mqtt.config;

import com.sakura.demo.mqtt.domain.MqttProperties;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttOutBoundConfiguration {

    @Resource
    private MqttProperties mqttProperties;
    @Resource
    private MqttPahoClientFactory mqttPahoClientFactory;


    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutboundMessageHandler() {
        MqttPahoMessageHandler mqttPahoMessageHandler = new MqttPahoMessageHandler(
                mqttProperties.getUrl(),
                mqttProperties.getPubClientId(),
                mqttPahoClientFactory
        );
        mqttPahoMessageHandler.setDefaultQos(0);
        mqttPahoMessageHandler.setAsync(true);
        mqttPahoMessageHandler.setDefaultTopic("default");
        return mqttPahoMessageHandler;
    }
}
