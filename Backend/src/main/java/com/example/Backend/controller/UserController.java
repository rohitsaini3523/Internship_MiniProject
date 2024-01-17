package com.example.Backend.controller;

import com.example.Backend.model.UserLogin;
import com.example.Backend.model.UserRegister;
import com.example.Backend.respository.UserRepository;
import com.example.Backend.services.LoginService;
import com.example.Backend.services.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Page;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.awt.print.Pageable;

@Slf4j
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
    public ResponseEntity loginUser(@RequestBody UserLogin userLogin){
        boolean userFound = this.loginService.login(userLogin);
        if(userFound)
        {
            log.info("User Login: {}",userLogin.getUsername());
            return new ResponseEntity<String>("User Logged in Successfully!", HttpStatus.ACCEPTED);
        }
        log.error("No User Found!");
        return new ResponseEntity<String>("User Not Found!", HttpStatus.NOT_FOUND);

    }
    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody UserRegister userRegister)
    {
        this.registerService.register(userRegister);
        return new ResponseEntity<String>("User Register Successfully!", HttpStatus.CREATED);

    }

}
