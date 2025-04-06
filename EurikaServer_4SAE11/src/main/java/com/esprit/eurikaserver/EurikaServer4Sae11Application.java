package com.esprit.eurikaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurikaServer4Sae11Application {

	public static void main(String[] args) {
		SpringApplication.run(EurikaServer4Sae11Application.class, args);
	}

}
