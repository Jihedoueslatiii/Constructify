package com.example.communicationmanagement.feignclient;

import com.example.communicationmanagement.entities.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Constructify") // Use the service name
public interface UserServiceClient {
    @GetMapping("/Constructify/user/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}