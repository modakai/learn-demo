package com.sakura.demo.mqtt.config;

import com.sakura.demo.mqtt.domain.MqttProperties;
import com.sakura.demo.mqtt.handler.ReceiverMessageHandler;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * 接收消息
 */
@Configuration
public class MqttInboundConfiguration {

    @Resource
    private MqttProperties mqttProperties;

    @Resource
    private MqttPahoClientFactory  mqttPahoClientFactory;

    @Resource
    public ReceiverMessageHandler receiverMessageHandler;

    @Bean
    // 消息通道
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter mqttPahoMessageDrivenChannelAdapter = new MqttPahoMessageDrivenChannelAdapter(
                mqttProperties.getUrl(),
                mqttProperties.getSubClientId(),
                mqttPahoClientFactory,
                mqttProperties.getSubTopic().split(",")
        );

        mqttPahoMessageDrivenChannelAdapter.setQos(1);
        mqttPahoMessageDrivenChannelAdapter.setConverter(new DefaultPahoMessageConverter());
        mqttPahoMessageDrivenChannelAdapter.setOutputChannel(mqttInboundChannel());
        return mqttPahoMessageDrivenChannelAdapter;
    }

    @Bean
    // 指定消息通道
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public MessageHandler handler() {
        return receiverMessageHandler;
    }
}
