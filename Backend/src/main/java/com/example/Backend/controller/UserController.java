package com.example.Backend.controller;

import com.example.Backend.exceptions.InvalidInputException;
import com.example.Backend.exceptions.UserNotFoundException;
import com.example.Backend.exceptions.UserRegistrationException;
import com.example.Backend.model.UserLogin;
import com.example.Backend.model.UserRegister;
import com.example.Backend.services.LoginServiceInterface;
import com.example.Backend.services.RegisterServiceInterface;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@Slf4j
@RestController
public class UserController {

    RegisterServiceInterface registerServiceInterface;
    LoginServiceInterface loginServiceInterface;

    UserController(RegisterServiceInterface registerServiceInterface, LoginServiceInterface loginServiceInterface) {
        this.registerServiceInterface = registerServiceInterface;
        this.loginServiceInterface = loginServiceInterface;
    }

    @Operation(summary = "Display user details", description = "Get user details by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Empty Username"),
            @ApiResponse(responseCode = "404",description = "User Doesn't exists"),
            @ApiResponse(responseCode = "200", description = "User Fetched Successfully")
    })
    @GetMapping("/user/{name}")
    public ResponseEntity<?> displayUser(@PathVariable(value = "name") String username) {
        UserLogin userLogin = this.loginServiceInterface.getUserDetails(username);
        switch (StringUtils.isEmpty(username)?"Empty": userLogin.getUsername()) {
            case "Empty" -> {
                log.info("Invalid Request");
                throw new InvalidInputException("Empty Username");
            }
            case "Not_Found" -> {
                log.error("User Doesn't exists");
                throw new UserNotFoundException("User Doesn't Exists");
            }
            default -> {
                log.info("User Fetched Successfully: {}", userLogin.getUsername());
            }
        }
        return new ResponseEntity<>(userLogin, HttpStatus.OK);
    }

    @Operation(summary = "Login user", description = "Authenticate user with provided credentials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "User Logged in Successfully!"),
            @ApiResponse(responseCode = "404", description = "User Doesn't Exist!"),
            @ApiResponse(responseCode = "401", description = "Wrong Password!")

    })
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody UserLogin userLogin, BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
            String loginServiceResponse = this.loginServiceInterface.login(userLogin);
            switch (bindingResult.hasErrors()?"Error" : loginServiceResponse) {
                case "Found" -> {
                    log.info("User Login: {}", userLogin.getUsername());
                    return new ResponseEntity<>("User Logged in Successfully!", HttpStatus.ACCEPTED);
                }
                case "Wrong Password" -> {
                    log.info("Wrong Password for user: {}", userLogin.getUsername());
                    throw new InvalidInputException("Wrong Password!");
                }
                case "Error" -> {
                    List<FieldError> errors = bindingResult.getFieldErrors();
                    errorMessage = new StringBuilder("Validation errors: ");
                    for (FieldError error : errors) {
                        errorMessage.append(error.getDefaultMessage()).append(". ");
                    }
                    throw new InvalidInputException(errorMessage.toString());
                }
                case "Not Found" -> {
                    log.info("User Doesn't Exists!");
                    throw new UserNotFoundException("User Doesn't Exist!");
                }
            }
        return new ResponseEntity<>(errorMessage.toString(), HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Register user", description = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully!"),
            @ApiResponse(responseCode = "409", description = "User Exists Already!"),
            @ApiResponse(responseCode = "400", description = "Insufficient Parameters!")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegister userRegister, BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
            String registerServiceResponse = this.registerServiceInterface.register(userRegister);
            switch(bindingResult.hasErrors()?"Error":registerServiceResponse){
                case "Registered"-> {
                    log.info("User Registered Successfully!");
                    return new ResponseEntity<>("User Registered Successfully!", HttpStatus.CREATED);
                }
                case "Error" -> {
                    List<FieldError> errors = bindingResult.getFieldErrors();
                    errorMessage = new StringBuilder("Validation errors: ");
                    for (FieldError error : errors) {
                        errorMessage.append(error.getDefaultMessage()).append(". ");
                    }
                    throw new InvalidInputException(errorMessage.toString());
                }
                case "Already Existed"->{
                    log.error("User Exists Already!");
                    throw new UserRegistrationException("User Exists Already!");
                }
            }
        return new ResponseEntity<>("User Could Not Be Registered! \n"+errorMessage , HttpStatus.BAD_REQUEST);
    }
}