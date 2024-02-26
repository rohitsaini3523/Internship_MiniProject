package com.example.Backend.controller;
import com.example.Backend.exceptions.UserNotFoundException;
import com.example.Backend.model.UserLogin;
import com.example.Backend.model.UserRegister;
import com.example.Backend.model.UserResponse;
import com.example.Backend.services.LoginServiceInterface;
import com.example.Backend.services.RegisterServiceInterface;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/user/")
public class UserController {

    RegisterServiceInterface registerServiceInterface;
    LoginServiceInterface loginServiceInterface;
    UserResponse userResponse = new UserResponse();
    UserController(RegisterServiceInterface registerServiceInterface, LoginServiceInterface loginServiceInterface) {
        this.registerServiceInterface = registerServiceInterface;
        this.loginServiceInterface = loginServiceInterface;
    }

    @Operation(summary = "Display user details", description = "Get user details by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",description = "User Doesn't exists"),
            @ApiResponse(responseCode = "200", description = "User Fetched Successfully")
    })
    @GetMapping("/{name}")
    public CompletableFuture<ResponseEntity<UserLogin>> displayUser(@PathVariable(value = "name") String username) {
        return loginServiceInterface.getUserDetails(username)
                .thenApply(userLogin -> ResponseEntity.ok().body(userLogin))
                .exceptionally(ex -> {throw new UserNotFoundException("User Not Registered!");});
    }


    @Operation(summary = "Register user", description = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully!"),
            @ApiResponse(responseCode = "409", description = "User Exists Already!"),
            @ApiResponse(responseCode = "401", description = "Insufficient Parameters!")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRegister userRegister) throws ExecutionException, InterruptedException {
        CompletableFuture<String> registrationFuture = registerServiceInterface.register(userRegister);
        registrationFuture.get();
        UserResponse userResponse = new UserResponse();
        log.info("User Registered Successfully!");
        userResponse.setMessage("User Registered Successfully!: " + userRegister.getUsername());
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

}