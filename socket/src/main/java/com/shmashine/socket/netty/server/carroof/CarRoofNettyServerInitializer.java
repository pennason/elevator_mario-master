package com.shmashine.socket.netty.server.carroof;

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
 * 连接初始化配置
 *
 * @author little.li
 */
@Component
public class CarRoofNettyServerInitializer extends ChannelInitializer<SocketChannel> {


    private final CarRoofNettyServerHandle carRoofNettyServerHandle;

    @Autowired
    public CarRoofNettyServerInitializer(CarRoofNettyServerHandle carRoofNettyServerHandle) {
        this.carRoofNettyServerHandle = carRoofNettyServerHandle;
    }


    @Override
    public void initChannel(SocketChannel channel) {
        ByteBuf delimiter = Unpooled.copiedBuffer("}".getBytes());

        channel.pipeline()
                .addLast("framer", new DelimiterBasedFrameDecoder(8192, delimiter))
                .addLast("decoder", new StringDecoder())
                .addLast("encoder", new StringEncoder())
                .addLast(new IdleStateHandler(180, 0, 0, TimeUnit.SECONDS))
                .addLast("handler", carRoofNettyServerHandle);
    }


}