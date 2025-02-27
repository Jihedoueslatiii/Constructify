package com.example.proj1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.proj1.services") // Spécifier le package de ProjectClient

public class Proj1Application {

    public static void main(String[] args) {
        SpringApplication.run(Proj1Application.class, args);
    }


}
