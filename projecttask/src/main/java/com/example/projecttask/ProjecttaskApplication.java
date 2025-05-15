package com.example.projecttask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.projecttask.services")
public class ProjecttaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjecttaskApplication.class, args);
    }

}
