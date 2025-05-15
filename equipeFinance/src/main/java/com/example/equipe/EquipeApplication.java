package com.example.equipe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
//@EnableFeignClients(basePackages = "com.example.equipe.services")
public class EquipeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EquipeApplication.class, args);
    }

}
