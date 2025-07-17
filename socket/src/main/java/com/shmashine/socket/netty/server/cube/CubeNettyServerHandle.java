package com.shmashine.socket.netty.server.cube;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shmashine.common.constants.SocketConstants;
import com.shmashine.common.utils.TimeUtils;
import com.shmashine.socket.message.ElevatorStatusHandle;
import com.shmashine.socket.message.MessageHandle;
import com.shmashine.socket.netty.ChannelManager;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 老版本设备连接后处理
 *
 * @author little.li
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class CubeNettyServerHandle extends SimpleChannelInboundHandler<String> {


    private final MessageHandle messageHandle;

    private final ElevatorStatusHandle elevatorStatusHandle;

    @Autowired
    public CubeNettyServerHandle(MessageHandle messageHandle, ElevatorStatusHandle elevatorStatusHandle) {
        this.messageHandle = messageHandle;
        this.elevatorStatusHandle = elevatorStatusHandle;
    }


    /**
     * 连接建立
     *
     * @param context channel上下文
     */
    @Override
    public void handlerAdded(ChannelHandlerContext context) {
        Channel channel = context.channel();
        // 连接建立时，向设备发送 need_login 命令
        channel.writeAndFlush(SocketConstants.CUBE_NEED_LOGIN);
    }


    /**
     * 收到客户端消息
     *
     * @param context channel上下文
     * @param message 客户端消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, String message) {
        Channel channel = context.channel();
        // 消息处理
        boolean isLogin = messageHandle.messageHandle(message, SocketConstants.SENSOR_TYPE_CUBE, channel);
        if (!isLogin) {
            // 设备未登录时，向设备发送 need_login 命令
            channel.writeAndFlush(SocketConstants.CUBE_NEED_LOGIN);
        }
    }


    /**
     * 60s没有收到设备信息，断开channel连接
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (IdleState.READER_IDLE.equals((event.state()))) {
                Channel channel = ctx.channel();
                String elevatorCode = ChannelManager.getElevatorCodeByChannel(channel);
                String sensorType = ChannelManager.getSensorTypeByChannel(channel);
                log.info("{} --- {} 3分钟未接收到设备消息，断开channel连接", TimeUtils.nowTime(), sensorType + "_" + elevatorCode);
                elevatorStatusHandle.offLineHandle(channel, SocketConstants.SERVER_PING_TIMEOUT);
            }
        }
        super.userEventTriggered(ctx, evt);
    }


    /**
     * 连接异常
     *
     * @param context   channel上下文
     * @param throwable 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) {
        Channel channel = context.channel();
        elevatorStatusHandle.offLineHandle(channel, SocketConstants.SERVER_CHANNEL_ERROR);
        throwable.printStackTrace();
    }


}
