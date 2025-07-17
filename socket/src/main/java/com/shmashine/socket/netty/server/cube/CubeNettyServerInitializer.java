package com.shmashine.socket.netty.server.cube;

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
public class CubeNettyServerInitializer extends ChannelInitializer<SocketChannel> {


    private final CubeNettyServerHandle cubeNettyServerHandle;

    @Autowired
    public CubeNettyServerInitializer(CubeNettyServerHandle cubeNettyServerHandle) {
        this.cubeNettyServerHandle = cubeNettyServerHandle;
    }


    @Override
    public void initChannel(SocketChannel channel) {
        ByteBuf delimiter = Unpooled.copiedBuffer("#13#".getBytes());

        channel.pipeline()
                .addLast("framer", new DelimiterBasedFrameDecoder(8192, delimiter))
                .addLast("decoder", new StringDecoder())
                .addLast("encoder", new StringEncoder())
                .addLast(new IdleStateHandler(180, 0, 0, TimeUnit.SECONDS))
                .addLast("handler", cubeNettyServerHandle);
    }


}