//package com.shmashine.fault.redis.listener;
//
//import com.shmashine.fault.elevator.service.TblElevatorService;
//import com.shmashine.fault.fault.service.TblFaultServiceI;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.connection.Message;
//import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
//import org.springframework.data.redis.listener.PatternTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.data.redis.listener.Topic;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
/// **
// * @author jiangheng
// * @version 1.0
// * @date 2021/8/16 18:02
// */
//@Component
//public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
//
//    @Value("${redis.listener.database}")
//    public String database;
//
//    @Resource
//    private TblFaultServiceI tblFaultServiceI;
//
//    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
//        super(listenerContainer);
//    }
//
//    @Override
//    protected void doRegister(RedisMessageListenerContainer listenerContainer) {
//        // 频道可以是多，多个传list
//        listenerContainer.addMessageListener(this, new PatternTopic("__keyevent@" + database + "__:expired"));
//    }
//
//    /**
//     * 针对redis数据失效事件，进行数据处理
//     */
//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//
//        // deviceTimeOut:MX3737:CarRoof
//        tblFaultServiceI.addDeviceTimeOutFault(message.toString());
//    }
//
//}
