package com.example.Backend.controller;

import com.example.Backend.model.UserLogin;
import com.example.Backend.model.UserRegister;
import com.example.Backend.services.LoginService;
import com.example.Backend.services.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Slf4j
@RestController
public class UserController {

    RegisterService registerService;
    LoginService loginService;

    UserController(RegisterService registerService, LoginService loginService) {
        this.registerService = registerService;
        this.loginService = loginService;
    }

    @Operation(summary = "Display user details", description = "Get user details by username")
    @GetMapping("/user/{name}")
    public UserLogin displayUser(@PathVariable("name") String name) {
        return this.loginService.getUserDetails(name);
    }

    @Operation(summary = "Login user", description = "Authenticate user with provided credentials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "User logged in successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLogin userLogin) {
        boolean userFound = this.loginService.login(userLogin);
        if (userFound) {
            log.info("User Login: {}", userLogin.getUsername());
            return new ResponseEntity<>("User Logged in Successfully!", HttpStatus.ACCEPTED);
        }
        log.error("No User Found!");
        return new ResponseEntity<>("User Not Found!", HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Register user", description = "Register a new user")
    @ApiResponse(responseCode = "201", description = "User registered successfully")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegister userRegister) {
        this.registerService.register(userRegister);
        return new ResponseEntity<>("User Registered Successfully!", HttpStatus.CREATED);
    }
}
