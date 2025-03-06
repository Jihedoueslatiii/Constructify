package com.esprit.pi.constructify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ConstructifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConstructifyApplication.class, args);
    }

}
