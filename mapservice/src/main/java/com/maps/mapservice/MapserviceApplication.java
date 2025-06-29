package com.maps.mapservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableCaching
public class MapserviceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(MapserviceApplication.class, args);

		Environment env = context.getEnvironment();

        System.out.println("===== Application Configuration =====");
        System.out.println("Active profile: " + String.join(", ", env.getActiveProfiles()));
        System.out.println("Application Name: " + env.getProperty("spring.application.name"));
        System.out.println("Redis Host: " + env.getProperty("spring.redis.host"));
        System.out.println("Redis Port: " + env.getProperty("spring.redis.port"));
        System.out.println("Eureka Host: " + env.getProperty("eureka.client.service-url.defaultZone"));
        System.out.println("=====================================");
	}

}
