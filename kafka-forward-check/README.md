# kafka消息转发验证

    使用Test 生产消息给 kafka.pro.internal.shmashine.com:9092 ， 
    之后在 kafka:9092 上消费该消息。
    中间 转发有 kafka-forward 模块负责