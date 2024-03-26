package com.example.Backend.controller;

import com.example.Backend.exceptions.InvalidInputException;
import com.example.Backend.model.UserContact;
import com.example.Backend.model.UserLogin;
import com.example.Backend.model.UserRegister;
import com.example.Backend.model.UserResponse;
import com.example.Backend.services.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/user/")
public class UserController {

    RegisterServiceImpl registerService;
    LoginServiceImpl loginService;
    ContactServiceImpl contactService;
    UserResponse userResponse = new UserResponse();

    UserController(RegisterServiceImpl registerService, LoginServiceImpl loginService, ContactServiceImpl contactService) {
        this.registerService = registerService;
        this.loginService = loginService;
        this.contactService = contactService;
    }

    @Operation(summary = "Display user details", description = "Get user details by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "User Doesn't exists"),
            @ApiResponse(responseCode = "200", description = "User Fetched Successfully")
    })
    @GetMapping("/{name}")
    public ResponseEntity<?> displayUser(@PathVariable(value = "name") String username) {
        UserLogin userLogin = this.loginService.getUserDetails(username);
        return new ResponseEntity<>(userLogin, HttpStatus.OK);
    }

    @Operation(summary = "Display user contact details", description = "Get user contact details by {username}/contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "User Doesn't exists"),
            @ApiResponse(responseCode = "200", description = "User Fetched Successfully")
    })
    @GetMapping("/{name}/contact")
    public ResponseEntity<?> displayUserContact(@PathVariable(value = "name") String username) {
        List<UserContact> userContacts = this.contactService.getContactDetails(username);
        if (userContacts.isEmpty()) {
            userResponse.setMessage("No contacts found for user: " + username);
            return new ResponseEntity<>(userResponse, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(userContacts, HttpStatus.OK);
        }
    }

    @Operation(summary = "Login user", description = "Authenticate user with provided credentials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "User Logged in Successfully!"),
            @ApiResponse(responseCode = "404", description = "User Doesn't Exist!"),
            @ApiResponse(responseCode = "401", description = "Wrong Password!")
    })
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid UserLogin userLogin) {
        this.loginService.login(userLogin);
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
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRegister userRegister) {
        this.registerService.register(userRegister);
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
    public ResponseEntity<?> addContactDetails(@PathVariable(value = "name") String name, @RequestBody @Valid UserContact userContact) {
        if (!userContact.getUsername().equals(name)) {
            throw new InvalidInputException("Invalid Request!");
        }
        this.contactService.addContactDetails(name, userContact);
        log.info("User Contact Detail Added!");
        userResponse.setMessage("User Contact Details Added!: " + userContact.getUsername());
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }
}