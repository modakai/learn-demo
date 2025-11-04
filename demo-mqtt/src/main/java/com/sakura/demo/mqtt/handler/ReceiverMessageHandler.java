package com.sakura.demo.mqtt.handler;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Component
public class ReceiverMessageHandler implements MessageHandler {
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        Object payload = message.getPayload();
        System.out.println("Received Message: " + payload);
        System.out.println("message: " + message);
    }
}
