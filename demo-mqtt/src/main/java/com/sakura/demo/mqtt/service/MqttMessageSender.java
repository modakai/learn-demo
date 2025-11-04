package com.sakura.demo.mqtt.service;

import com.sakura.demo.mqtt.gateway.MqttGateway;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class MqttMessageSender {

    @Resource
    private MqttGateway mqttGateway;

    public void sendMessage(String topic, String payload){
        mqttGateway.sendMessage(topic, payload);
    }

    public void sendMessage(String topic, int qos, String payload){
        mqttGateway.sendMessage(topic, qos, payload);
    }
}
