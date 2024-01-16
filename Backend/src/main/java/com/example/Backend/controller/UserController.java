package com.example.Backend.controller;

import com.example.Backend.model.UserLogin;
import com.example.Backend.model.UserRegister;
import com.example.Backend.services.LoginService;
import com.example.Backend.services.RegisterService;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    RegisterService registerService;
    LoginService loginService;
    UserController(RegisterService registerService,LoginService loginService)
    {
        this.registerService = registerService;
        this.loginService = loginService;
    }
    @GetMapping("/user/{name}")
    public UserLogin displayUser(@PathVariable("name") String name)
    {
        return this.loginService.getUserDetails(name);
    }
    @PostMapping("/login")
    public void loginUser(@RequestBody UserLogin userLogin){
        boolean userFound = this.loginService.login(userLogin);
        if(userFound)
        {
            System.out.println("User Login :"+ userLogin.getUsername());
        }
        else
        {
            System.out.println("No User Found!");
        }
    }
    @PostMapping("/register")
    public void registerUser(@RequestBody UserRegister userRegister)
    {
        this.registerService.register(userRegister);
    }
}
