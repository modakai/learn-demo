package com.sakura.demo.mqtt.gateway;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {

    void sendMessage(@Header(MqttHeaders.TOPIC) String topic, String payload);

    void sendMessage(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS)int qos, String payload);
}
