package com.shmashine.socket.netty.server.carroof_TLS;

import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * nettyServer启动类 轿顶设备连接
 *
 * @author little.li
 */
@Slf4j
@Component
public class CarRoofNettyTLSServer {


    ChannelFuture channelFuture;

    /**
     * boss 线程组用于处理连接工作
     */
    private final EventLoopGroup boss = new NioEventLoopGroup();

    /**
     * work 线程组用于数据处理
     */
    private final EventLoopGroup work = new NioEventLoopGroup();

    @Value("${netty.car_roof_tls.port}")
    private Integer port;

    private final CarRoofNettyTLSServerInitializer carRoofNettyServerInitializer;

    @Autowired
    public CarRoofNettyTLSServer(CarRoofNettyTLSServerInitializer carRoofNettyServerInitializer) {
        this.carRoofNettyServerInitializer = carRoofNettyServerInitializer;
    }


    /**
     * PostConstruct注解来表示该方法在 Spring初始化CarRoofNettyServer类后调用
     */
    @PostConstruct
    public void start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, work)
                // 指定Channel
                .channel(NioServerSocketChannel.class)
                //使用指定的端口设置套接字地址
                .localAddress(new InetSocketAddress(port))

                //服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
                .option(ChannelOption.SO_BACKLOG, 1024)

                //设置TCP长连接,一般如果两个小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true)

                //将小的数据包包装成更大的帧进行传送，提高网络的负载,即TCP延迟传输
                .childOption(ChannelOption.TCP_NODELAY, true)

                .childHandler(carRoofNettyServerInitializer);

        channelFuture = bootstrap.bind().sync();

    }


    /**
     * 关闭Channel服务
     */
    public void close() {
        try {
            channelFuture.channel().close();
            boss.shutdownGracefully();
            work.shutdownGracefully();
            log.info("The CarRoofNettyServer Is Close");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}



