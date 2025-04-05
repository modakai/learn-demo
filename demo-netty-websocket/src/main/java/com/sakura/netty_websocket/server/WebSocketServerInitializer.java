package com.sakura.netty_websocket.server;

import com.sakura.netty_websocket.hanlder.WebSocketFrameHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;


public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        
        // HTTP编解码器
        pipeline.addLast(new HttpServerCodec());
        // 聚合HTTP请求
        pipeline.addLast(new HttpObjectAggregator(65536));
        // WebSocket协议处理器
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        // 自定义业务处理器
        pipeline.addLast(new WebSocketFrameHandler());
    }
}