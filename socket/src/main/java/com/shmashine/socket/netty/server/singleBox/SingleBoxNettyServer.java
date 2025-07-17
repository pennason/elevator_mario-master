package com.shmashine.socket.netty.server.singleBox;

import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * 迅达别墅梯设备连接
 *
 * @author jiangheng
 * @version 1.0 - 2021/4/9 11:15
 */
@Slf4j
@Component
public class SingleBoxNettyServer {

    ChannelFuture channelFuture;

    /**
     * boss线程 用于处理连接工作
     */
    private final EventLoopGroup boss = new NioEventLoopGroup(64);

    /**
     * work线程 用于处理数据
     */
    private final EventLoopGroup work = new NioEventLoopGroup(128);

    @Value("${netty.SingleBox.port}")
    private Integer port;

    private final SingleBoxNettyServerInitialize nettyServerInitialize;

    @Autowired
    public SingleBoxNettyServer(SingleBoxNettyServerInitialize eNettyServerInitialize) {
        this.nettyServerInitialize = eNettyServerInitialize;
    }

    /**
     * 初始化
     */
    @PostConstruct
    public void start() throws InterruptedException {

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(boss, work)
                //指定Channel
                .channel(NioServerSocketChannel.class)
                //使用指定的端口设置套接字地址
                .localAddress(new InetSocketAddress(port))
                //服务端可连接队列数
                .option(ChannelOption.SO_BACKLOG, 10000)
                .option(ChannelOption.SO_SNDBUF, 20 * 1024 * 1024)
                .option(ChannelOption.SO_RCVBUF, 20 * 1024 * 1024)
                .option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(1024 * 1024, 2 * 1024 * 1024))
                //设置TCP长连接，如果两小时没有数据通信时，TCP会发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //将小数据包包装成更大的帧进行传送，提高网络负载，即tcp延迟传输
                .childOption(ChannelOption.TCP_NODELAY, true)
                //初始化
                .childHandler(nettyServerInitialize);

        //绑定端口，开始接收进来的连接
        channelFuture = serverBootstrap.bind().sync();
    }

    /**
     * 关闭channel服务
     */
    public void close() {

        try {
            channelFuture.channel().close();
            boss.shutdownGracefully();
            work.shutdownGracefully();
            log.info("the singleBoxNettyServer is close");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
