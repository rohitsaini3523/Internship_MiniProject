package com.example.Backend.controller;
import com.example.Backend.exceptions.InvalidInputException;
import com.example.Backend.exceptions.UserNotFoundException;
import com.example.Backend.model.UserContact;
import com.example.Backend.model.UserLogin;
import com.example.Backend.model.UserRegister;
import com.example.Backend.model.UserResponse;
import com.example.Backend.services.ContactServiceInterface;
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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/user/")
public class UserController {

    RegisterServiceInterface registerServiceInterface;
    LoginServiceInterface loginServiceInterface;
    ContactServiceInterface contactServiceInterface;
    UserResponse userResponse = new UserResponse();
    UserController(RegisterServiceInterface registerServiceInterface, LoginServiceInterface loginServiceInterface,ContactServiceInterface contactServiceInterface) {
        this.registerServiceInterface = registerServiceInterface;
        this.loginServiceInterface = loginServiceInterface;
        this.contactServiceInterface = contactServiceInterface;
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
    @Operation(summary = "Display user contact details", description = "Get user contact details by {username}/contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",description = "User Doesn't exists"),
            @ApiResponse(responseCode = "200", description = "User Fetched Successfully")
    })
    @GetMapping("/{name}/contact")
    public CompletableFuture<ResponseEntity<?>> displayUserContact(@PathVariable(value = "name") String username) {
        CompletableFuture<List<UserContact>> userContactsFuture = this.contactServiceInterface.getContactDetails(username);
        return userContactsFuture.thenApply(userContacts -> {
            if (!userContacts.isEmpty()) {
                log.info("User Contact Details Fetched: {}",username);
                return new ResponseEntity<>(userContacts, HttpStatus.OK);
            } else {
                throw new UserNotFoundException("User Contact Details Doesn't Exists");
            }
        });
    }

    @Operation(summary = "Login user", description = "Authenticate user with provided credentials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "User Logged in Successfully!"),
            @ApiResponse(responseCode = "404", description = "User Doesn't Exist!"),
            @ApiResponse(responseCode = "401", description = "Wrong Password!")
    })
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid UserLogin userLogin) throws ExecutionException, InterruptedException{
        log.info("Thread info: {}",Thread.currentThread());
        CompletableFuture<String> loginFuture = loginServiceInterface.login(userLogin);
        String result = loginFuture.get();
        log.info("User Login: {}", userLogin.getUsername());
        userResponse.setMessage("User Logged in: " + userLogin.getUsername());
        return new ResponseEntity<>(userResponse, HttpStatus.ACCEPTED);
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

    @Operation(summary = "user Contact Update", description = "Add user contact details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User Contact Details Added successfully!"),
            @ApiResponse(responseCode = "409", description = "User Contact Details Exists Already!"),
            @ApiResponse(responseCode = "401", description = "Insufficient Parameters!")
    })
    @PostMapping("/{name}/add/contact")
    public ResponseEntity<?> addContactDetails(@PathVariable(value="name") String name, @RequestBody @Valid UserContact userContact) throws ExecutionException, InterruptedException {
        if (!userContact.getUsername().equals(name)) {
            throw new InvalidInputException("Invalid Request!");
        }
        CompletableFuture<String> addContactFuture = contactServiceInterface.addContactDetails(name, userContact);
        String result = addContactFuture.get();
        log.info("User Contact Detail Added!");
        userResponse.setMessage("User Contact Details Added!: " + userContact.getUsername());
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }
}