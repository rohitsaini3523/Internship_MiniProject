package com.example.Backend.controller;
import com.example.Backend.exceptions.InvalidInputException;
import com.example.Backend.model.UserContact;
import com.example.Backend.model.UserLogin;
import com.example.Backend.model.UserRegister;
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

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/user/")
public class UserController {

    RegisterServiceInterface registerServiceInterface;
    LoginServiceInterface loginServiceInterface;
    ContactServiceInterface contactServiceInterface;
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
    public ResponseEntity<?> displayUser(@PathVariable(value = "name") String username) {
        UserLogin userLogin = this.loginServiceInterface.getUserDetails(username);
        return new ResponseEntity<>(userLogin, HttpStatus.OK);
    }

    @Operation(summary = "Login user", description = "Authenticate user with provided credentials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "User Logged in Successfully!"),
            @ApiResponse(responseCode = "404", description = "User Doesn't Exist!"),
            @ApiResponse(responseCode = "401", description = "Wrong Password!")
    })
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody @Valid  UserLogin userLogin) {
        this.loginServiceInterface.login(userLogin);
        log.info("User Login: {}", userLogin.getUsername());
        return new ResponseEntity<>("User Logged in Successfully!", HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Register user", description = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully!"),
            @ApiResponse(responseCode = "409", description = "User Exists Already!"),
            @ApiResponse(responseCode = "401", description = "Insufficient Parameters!")
    })
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegister userRegister) {
        this.registerServiceInterface.register(userRegister);
        log.info("User Registered Successfully!");
        return new ResponseEntity<>("User Registered Successfully!", HttpStatus.CREATED);

    }

    @PostMapping("/{name}/contact")
    public ResponseEntity<String> addContactDetails(@PathVariable(value="name") String name, @RequestBody @Valid UserContact userContact){
        if(!userContact.getUsername().equals(name))
        {
            throw new InvalidInputException("Invalid Request!");
        }
        this.contactServiceInterface.addContactDetails(name, userContact);
        log.info("User Contact Detail Added!");
        return new ResponseEntity<>("User Contact Details Added!",HttpStatus.CREATED);
    }
}