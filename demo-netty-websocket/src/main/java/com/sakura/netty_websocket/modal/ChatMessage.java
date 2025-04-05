package com.sakura.netty_websocket.modal;

public class ChatMessage {
    private String type; // 消息类型：LOGIN, CHAT, LOGOUT
    private String sender;
    private String receiver;
    private String content;
    private String timestamp;

    public ChatMessage() {
    }

    public ChatMessage(String type, String sender, String receiver, String content, String timestamp) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}