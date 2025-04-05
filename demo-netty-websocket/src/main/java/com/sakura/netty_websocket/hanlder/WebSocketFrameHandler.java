package com.sakura.netty_websocket.hanlder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakura.netty_websocket.modal.ChatMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final Map<ChannelId, String> users = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        ChatMessage msg = objectMapper.readValue(frame.text(), ChatMessage.class);

        switch (msg.getType()) {
            case "LOGIN":
                users.put(ctx.channel().id(), msg.getSender());
                broadcastSystemMessage(msg.getSender() + " 进入聊天室");
                break;
            case "CHAT":
                if ("ALL".equals(msg.getReceiver())) {
                    broadcastMessage(msg);
                } else {
                    sendPrivateMessage(msg);
                }
                break;
            case "LOGOUT":
                users.remove(ctx.channel().id());
                broadcastSystemMessage(msg.getSender() + " 离开聊天室");
                ctx.channel().close();
                break;
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        String user = users.remove(ctx.channel().id());
        if (user != null) {
            broadcastSystemMessage(user + " 异常断开");
        }
        channels.remove(ctx.channel());
    }

    private void broadcastSystemMessage(String content) {
        try {
            // 构建系统消息对象
            ChatMessage systemMsg = new ChatMessage();
            systemMsg.setType("SYSTEM");
            systemMsg.setSender("[系统通知]");
            systemMsg.setContent(content);
            systemMsg.setTimestamp(String.valueOf(System.currentTimeMillis()));

            // 转换为JSON并广播
            String json = objectMapper.writeValueAsString(systemMsg);
            channels.writeAndFlush(new TextWebSocketFrame(json));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void broadcastMessage(ChatMessage msg) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(msg);
        channels.writeAndFlush(new TextWebSocketFrame(json));
    }

    private void sendPrivateMessage(ChatMessage msg) {
        // 实现私聊逻辑（根据receiver查找对应Channel）
        channels.stream()
                .filter(channel -> users.get(channel.id()).equals(msg.getReceiver()))
                .findFirst()
                .ifPresent(targetChannel -> {
                    try {
                        String json = objectMapper.writeValueAsString(msg);
                        targetChannel.writeAndFlush(new TextWebSocketFrame(json));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
    }
}