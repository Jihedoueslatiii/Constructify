package com.example.communicationmanagement.feignclient;

import com.example.communicationmanagement.entities.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "Constructify")
public interface UserServiceClient {
    @GetMapping("/Constructify/user/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);

    @GetMapping("/Constructify/users/search")
    List<UserDTO> getUsersByEmailPrefix(@RequestParam("prefix") String prefix);
}