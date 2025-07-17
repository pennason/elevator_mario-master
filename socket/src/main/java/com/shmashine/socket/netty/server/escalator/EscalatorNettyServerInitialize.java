package com.shmashine.socket.netty.server.escalator;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * EscalatorNettyServerInitialize
 *
 * @author jiangheng
 * @version 1.0 -  2021/4/9 11:12
 */
@Component
public class EscalatorNettyServerInitialize extends ChannelInitializer<SocketChannel> {

    private final EscalatorNettyServerHandle escalatorNettyServerHandle;

    @Autowired
    public EscalatorNettyServerInitialize(EscalatorNettyServerHandle escalatorNettyServerHandle) {
        this.escalatorNettyServerHandle = escalatorNettyServerHandle;
    }

    @Override
    protected void initChannel(SocketChannel ch) {

        //设置连接分隔符
        ByteBuf delimiter = Unpooled.copiedBuffer("}".getBytes());

        ch.pipeline()
                //处理TCP粘包问题，自定义分隔符
                .addLast("framer", new DelimiterBasedFrameDecoder(8192, delimiter))
                //解码request
                .addLast("decoder", new StringDecoder())
                //编码response
                .addLast("encoder", new StringEncoder())
                //心跳机制 :180s没有收到设备消息，断开连接
                .addLast(new IdleStateHandler(180, 0, 0, TimeUnit.SECONDS))
                //消息处理接口
                .addLast("handler", escalatorNettyServerHandle);
    }
}