package com.shmashine.gateway;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson2.JSON;

@SpringBootTest
class GatewayApplicationTests {


    @Test
    void contextLoads() {
    }


    /**
     * Redis中gateway route前缀
     */
    private static final String GATEWAY_ROUTES_PREFIX = "GATEWAY_ROUTE_";

    @Resource
    private StringRedisTemplate redisTemplate;

    // route配置列表
    private Set<RouteDefinition> routeDefinitions = new HashSet<>();

    /**
     * 获取Redis中路由URL
     */
    @Test
    void getRedisStringTest() {
        // 从Redis中获取路由信息
        Set<String> gatewayKeys = redisTemplate.keys(GATEWAY_ROUTES_PREFIX + "*");
        if (!CollectionUtils.isEmpty(gatewayKeys)) {
            // 将Redis中的路由信息加载到本地
            List<String> gatewayRoutes = Optional.ofNullable(
                    redisTemplate.opsForValue().multiGet(gatewayKeys)).orElse(Lists.newArrayList());
            gatewayRoutes.forEach(routeDefinition ->
                    routeDefinitions.add(
                            JSON.parseObject(routeDefinition, RouteDefinition.class)
                    ));
            System.out.println(routeDefinitions);
        }
    }


    /**
     * 获取Redis中路由URL
     */
    @Test
    void setRedisGatewayString() {
        // 从Redis中获取路由信息
        String code = "MX3391";
        String key = GATEWAY_ROUTES_PREFIX + code;
        String value = "{uri: 'http://47.104.215.210:8083/login', "
                + "predicates: [{name: 'Path', args: {pattern: '/ws/" + code + "'}}]}";

        ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();
        stringValueOperations.set(key, value);


        System.out.println(routeDefinitions);
    }

}
