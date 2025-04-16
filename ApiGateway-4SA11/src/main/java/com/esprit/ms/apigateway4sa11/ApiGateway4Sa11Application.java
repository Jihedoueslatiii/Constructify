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
	public RouteLocator getRouteApiGateway(RouteLocatorBuilder builder)
	{
		return builder.routes()
				// Add route for HEBERGEMENT service
				.route("CONSTRUCTIFY", r -> r.path("/Constructify/user/**")
						.uri("lb://CONSTRUCTIFY"))

				.route("PIDEV-EQUIPE", r -> r.path("/PIDEV-equipe/teams/**")
						.or()
						.path("/PIDEV-equipe/api/ratings/**")
						.uri("lb://PIDEV-EQUIPE"))

				.route("PROJECT", r -> r.path("/project/api/projets/**")
						.or()
						.path("/project/api/tasks/**")
						.uri("lb://PROJECT"))



				.build();



	}
}
