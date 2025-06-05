package com.maps.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
            .build();

    }
    
}
