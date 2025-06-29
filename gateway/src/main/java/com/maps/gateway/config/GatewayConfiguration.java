package com.maps.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfiguration {

    @Bean
    public RouteLocator mapRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("search", p -> p
                        .path("/search")
                        .uri("lb://handler"))
                .route("getmap", p -> p
                        .path("/getmap")
                        .uri("lb://mapservice"))
                .route("ping", r -> r
                        .path("/ping")
                        .filters(f -> f.setResponseHeader("Content-Type", "text/plain")
                                .rewritePath("/ping", "/") // optional cleanup
                                .modifyResponseBody(String.class, String.class,
                                        (exchange, body) -> Mono.just("pong")))
                        .uri("no://op")) // dummy URI, won't be used
                .route("returnpoint", r -> r
                        .path("/returnpoint")
                        .filters(f -> f.setResponseHeader("Content-Type", "application/json")
                                .modifyResponseBody(String.class, String.class,
                                        (exchange, body) -> Mono.just("{\"x\":123,\"y\":456}")))
                        .uri("no://op"))
                .build();

    }

}
