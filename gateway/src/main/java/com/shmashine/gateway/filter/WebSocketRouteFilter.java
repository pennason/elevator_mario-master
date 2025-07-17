package com.shmashine.gateway.filter;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;

import com.shmashine.gateway.redis.RedisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


/**
 * Gateway路由过滤器 - 对websocket连接进行路由
 *
 * @author little.li
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class WebSocketRouteFilter implements GatewayFilter {
    private final RedisService redisService;

    //@Value("${spring.redis.host}")
    //private String host;

    /**
     * socket类型 all: 通过一个socket获取该电梯所有设备的消息
     */
    private static final String SOCKET_TYPE_ALL = "all";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 获取电梯编号 ws://47.105.214.0:8001/ws/Monitor/{sensorType} or "all" /{elevatorCode} or {registerNum}
        String urlSuffix = getUrlSuffix(exchange);
        String elevatorCode = getElevatorCode(urlSuffix);
        String sensorType = getSensorType(urlSuffix);
        //log.info("redis host is {}", host);
        log.info("filter exchange {}, {}, {}", urlSuffix, elevatorCode, sensorType);
        // 从Redis中获取路由节点信息
        String targetNode = "";
        if (SOCKET_TYPE_ALL.equals(sensorType)) {
            Map<Object, Object> registerStatus = redisService.getRegisterStatus(elevatorCode);
            if (!registerStatus.isEmpty()) {
                String nodeIp = String.valueOf(registerStatus.get("server_ip"));
                String nodePort = String.valueOf(registerStatus.get("server_port"));
                //默认端口8002
                nodePort = !StringUtils.hasText(nodePort) || "null".equals(nodePort) ? "8002" : nodePort;
                // 将registerNum替换为elevatorCode
                urlSuffix = registerToElevatorCode(urlSuffix, registerStatus.get("elevator_code").toString());
                targetNode = "http://" + nodeIp + ":" + nodePort + "/" + urlSuffix;
            }
        } else {
            Map<Object, Object> deviceStatus = redisService.getDeviceStatus(elevatorCode, sensorType);
            //log.info("filter exchange get redis device status {}", deviceStatus.toString());
            String nodeIp = deviceStatus.get("server_ip") == null ? "" : String.valueOf(deviceStatus.get("server_ip"));
            String nodePort = deviceStatus.get("server_port") == null ? "" :
                    String.valueOf(deviceStatus.get("server_port"));

            //默认端口8002
            nodePort = StringUtils.hasText(nodePort) ? nodePort : "8002";
            targetNode = "http://" + (StringUtils.hasText(nodeIp) ? nodeIp :
                    "172.31.183.100") + ":" + nodePort + "/" + urlSuffix;

            log.info("filter exchange build target  {}", targetNode);
        }


        // 构建URI对象
        URI uri = UriComponentsBuilder.fromHttpUrl(targetNode).build().toUri();

        log.info("浏览器连接WebSocket: {}", uri);

        // 基于原有路由构建出新的路由（路由到指定服务器）
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);

        assert route != null;

        Route redisRoute = Route.async()
                .asyncPredicate(route.getPredicate())
                .filters(route.getFilters())
                .id(route.getId())
                .order(route.getOrder())
                .uri(uri).build();

        ServerHttpRequest request = exchange.getRequest().mutate().uri(uri).build();
        exchange.getAttributes().put(GATEWAY_ROUTE_ATTR, redisRoute);
        return chain.filter(exchange.mutate().request(request).build());
    }


    /**
     * 过滤器优先级 返回值越小，优先级越高
     *
     * @return 0
     */
    /*@Override
    public int getOrder() {
        return -2;
    }
*/

    ////////////////////////////////////private method///////////////////////////////////////////


    /**
     * 获取URL后缀
     *
     * @return 例：/ws/Monitor/CarRoof/MX3769
     */
    private String getUrlSuffix(ServerWebExchange exchange) {
        URI uri = exchange.getRequest().getURI();
        String path = uri.getRawPath();
        return path.substring(path.indexOf("/") + 1);
        //return uri.getRawPath();
    }


    /**
     * 获取请求路径中的电梯编号
     *
     * @return 电梯编号
     */
    private String getElevatorCode(String urlSuffix) {
        String[] split = urlSuffix.split("/");
        return split[3];
    }


    private String registerToElevatorCode(String urlSuffix, String elevatorCode) {
        String registerNum = urlSuffix.substring(urlSuffix.lastIndexOf("/") + 1);
        return urlSuffix.replace(registerNum, elevatorCode);
    }

    /**
     * 获取请求路径中的设备类型
     *
     * @return 设备类型
     */
    private String getSensorType(String urlSuffix) {
        String[] split = urlSuffix.split("/");
        return split[2];
    }


}
