package com.example.Backend.controller;

import com.example.Backend.model.User;
import com.example.Backend.services.RegisterService;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    RegisterService registerService;
    UserController(RegisterService service)
    {
        this.registerService = service;
    }
    @GetMapping("/user/{name}")
    public void displayUser(@PathVariable("name") String name)
    {
        //display user detail
    }
    @PostMapping("/register")
    public void registerUser(@RequestBody User user)
    {
        this.registerService.register(user);
    }
}
