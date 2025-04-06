package com.esprit.ms.apigateway4sa11;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGateway4Sa11Application {

	public static void main(String[] args) {
		SpringApplication.run(ApiGateway4Sa11Application.class, args);
	}

	@Bean
	public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(r -> r.path("/fournisseurs/**")
						.filters(f -> f.rewritePath(
								"/fournisseurs/(?<remaining>.*)",
								"/SupplierContracts/${remaining}"))
						.uri("lb://fournisseurs-web"))
				.build();
	}
}