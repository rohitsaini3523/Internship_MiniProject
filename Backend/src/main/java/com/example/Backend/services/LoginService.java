package com.example.Backend.services;

import com.example.Backend.entity.UserRegisterDetails;
import com.example.Backend.exceptions.InvalidInputException;
import com.example.Backend.exceptions.UserNotFoundException;
import com.example.Backend.model.UserLogin;
import com.example.Backend.respository.UserRepository;
import com.example.Backend.validator.UserLoginValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class LoginService implements LoginServiceInterface{
    UserRepository userRepository;
    UserLoginValidator userLoginValidator;
    LoginService(UserRepository userRepository, UserLoginValidator userLoginValidator)
    {
        this.userRepository = userRepository;
        this.userLoginValidator = userLoginValidator;
    }
    @Async("MultiRequestAsyncThread")
    @Override
    public CompletableFuture<UserLogin> getUserDetails(String name) {
        CompletableFuture<UserLogin> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            UserRegisterDetails userRegisterDetails = userRepository.findByUsername(name);
            if (userRegisterDetails != null) {
                UserLogin userLogin = UserLogin.builder()
                        .username(userRegisterDetails.getUsername())
                        .password(userRegisterDetails.getPassword())
                        .build();
                future.complete(userLogin);
            } else {
                future.completeExceptionally(new UserNotFoundException("User doesn't exist"));
            }
        });

        return future;
    }
    @Async("MultiRequestAsyncThread")
    @Override
    public CompletableFuture<String> login(UserLogin userLogin) {
        return CompletableFuture.supplyAsync(() -> {
            Errors errors = new BeanPropertyBindingResult(userLogin, "userLogin");
            userLoginValidator.validate(userLogin, errors);
            if (errors.hasErrors()) {
                throw new InvalidInputException("Invalid login details");
            }

            UserRegisterDetails userRegisterDetails = userRepository.findByUsernameAndPassword(userLogin);
            UserRegisterDetails findUsername = userRepository.findByUsername(userLogin.getUsername());
            if (findUsername != null) {
                if (userRegisterDetails != null) {
                    log.info("User Logged in is: {}!", userLogin.getUsername());
                    return "Found";
                } else {
                    throw new InvalidInputException("Wrong Password!");
                }
            } else {
                throw new UserNotFoundException("User doesn't exist");
            }
        });
    }

}
