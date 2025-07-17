package com.shmashine.socket.netty.server.carroof_TLS;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shmashine.socket.netty.server.ssl.ServerSSLFactory;

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
public class CarRoofNettyTLSServerInitializer extends ChannelInitializer<SocketChannel> {


    private final CarRoofNettyTLSServerHandle carRoofNettyServerHandle;

    @Autowired
    public CarRoofNettyTLSServerInitializer(CarRoofNettyTLSServerHandle carRoofNettyServerHandle) {
        this.carRoofNettyServerHandle = carRoofNettyServerHandle;
    }


    @Override
    public void initChannel(SocketChannel channel) throws SSLException {

        ByteBuf delimiter = Unpooled.copiedBuffer("}".getBytes());

        channel.pipeline()
                .addFirst(ServerSSLFactory.sslContext().newHandler(channel.alloc()))
                .addLast("framer", new DelimiterBasedFrameDecoder(8192, delimiter))
                .addLast("decoder", new StringDecoder())
                .addLast("encoder", new StringEncoder())
                .addLast(new IdleStateHandler(180, 0, 0, TimeUnit.SECONDS))
                .addLast("handler", carRoofNettyServerHandle);
    }


}