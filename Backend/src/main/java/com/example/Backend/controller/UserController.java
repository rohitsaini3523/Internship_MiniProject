package com.example.Backend.controller;

import com.example.Backend.model.UserLogin;
import com.example.Backend.model.UserRegister;
import com.example.Backend.services.LoginService;
import com.example.Backend.services.RegisterService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Empty Username"),
            @ApiResponse(responseCode = "404",description = "User Doesn't exists"),
            @ApiResponse(responseCode = "200", description = "User Fetched Successfully")
    })
    @GetMapping("/user/{name}")
    public ResponseEntity<UserLogin> displayUser(@PathVariable(value = "name") String name) {
        if(name.isEmpty())
        {
            log.info("Invalid Request");
            return new ResponseEntity<>(UserLogin.builder().username("Empty Username").build(),HttpStatus.BAD_REQUEST);
        }
        else {
            UserLogin userLogin = this.loginService.getUserDetails(name);
            if (userLogin.getUsername().equals("Not_Found")) {
                log.error("User Doesn't exists");
                return new ResponseEntity<>(userLogin, HttpStatus.NOT_FOUND);
            } else {
                log.info("User Fetched Successfully: {}", userLogin.getUsername());
                return new ResponseEntity<>(userLogin, HttpStatus.OK);
            }
        }
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
        try {
            String loginServiceResponse = this.loginService.login(userLogin);
            if (loginServiceResponse.equals("Found")) {
                log.info("User Login: {}", userLogin.getUsername());
                return new ResponseEntity<>("User Logged in Successfully!", HttpStatus.ACCEPTED);
            }
            else {
                if (bindingResult.hasErrors()) {
                    List<FieldError> errors = bindingResult.getFieldErrors();
                    errorMessage = new StringBuilder("Validation errors: ");
                    for (FieldError error : errors) {
                        errorMessage.append(error.getDefaultMessage()).append("\n");
                    }
                    throw new MethodArgumentNotValidException(null, bindingResult);
                }
                else if(loginServiceResponse.equals("Wrong Password"))
                {
                    log.info("Wrong Password for user: {}", userLogin.getUsername());
                    return new ResponseEntity<>("Wrong Password!", HttpStatus.UNAUTHORIZED);
                }
                else if(loginServiceResponse.equals("Not Found"))
                {
                    log.info("User Doesn't Exists!");
                    return new ResponseEntity<>("User Doesn't Exist!", HttpStatus.NOT_FOUND);
                }
            }
        } catch (MethodArgumentNotValidException e) {
            log.error("Validation Errors: {}", errorMessage);
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
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegister userRegister, BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        try {
            String registerServiceResponse = this.registerService.register(userRegister);
            if (registerServiceResponse.equals("Registered")) {
                log.info("User Registered Successfully!");
                return new ResponseEntity<>("User Registered Successfully!", HttpStatus.CREATED);
            }
            else {
                if (bindingResult.hasErrors()) {
                    List<FieldError> errors = bindingResult.getFieldErrors();
                    errorMessage = new StringBuilder("Validation errors: ");
                    for (FieldError error : errors) {
                        errorMessage.append(error.getDefaultMessage()).append("\n");
                    }
                    throw new MethodArgumentNotValidException(null, bindingResult);
                }
                else if(registerServiceResponse.equals("Already Existed"))
                {
                    log.error("User Exists Already!");
                    return new ResponseEntity<>("User Exists Already!", HttpStatus.CONFLICT);
                }
            }
        } catch (MethodArgumentNotValidException e) {
            log.error("Validation Errors: {}", errorMessage);
        }
        return new ResponseEntity<>("User Could Not Be Registered! \n"+errorMessage , HttpStatus.BAD_REQUEST);
    }
}