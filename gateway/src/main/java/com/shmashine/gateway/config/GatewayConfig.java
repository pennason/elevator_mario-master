package com.shmashine.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shmashine.gateway.filter.WebSocketRouteFilter;

import lombok.RequiredArgsConstructor;


/**
 * gateway路由配置
 *
 * @author little.li
 */
@Configuration
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GatewayConfig {
    private final WebSocketRouteFilter webSocketRouteFilter;


    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/ws/**")
                        //.filters(f -> f.filter(new WebSocketRouteFilter(redisService)))
                        .filters(f -> f.filter(webSocketRouteFilter))
                        .uri("ws://shmashine-api:8080/provider/test"))

                .route("pm-service", r -> r.path("/api/pm/**")
                        .uri("lb://SHMASHINE-PM/api/pm"))

                .route("api-service", r -> r.path("/api/**")
                        .uri("lb://SHMASHINE-API/api"))

                .route("auth-service", r -> r.path("/auth/**")
                        .uri("lb://SHMASHINE-AUTH/auth"))
                .route("camera-service", r -> r.path("/camera/**")
                        .uri("lb://SHMASHINE-CAMERA/"))
                .route("common-big-screen-server", r -> r.path("/commonBigScreen/**")
                        .uri("lb://SHMASHINE-COMMON-BIG-SCREEN/"))

                .build();

    }

}
