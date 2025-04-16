package com.esprit.pi.pidevequipe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PidevEquipeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PidevEquipeApplication.class, args);
    }

}
