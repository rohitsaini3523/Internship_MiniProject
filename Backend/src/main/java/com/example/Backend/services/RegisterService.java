package com.example.Backend.services;

import com.example.Backend.entity.UserRegisterDetails;
import com.example.Backend.exceptions.InvalidInputException;
import com.example.Backend.model.UserRegister;
import com.example.Backend.respository.UserRepository;
import com.example.Backend.validator.UserRegisterValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class RegisterService implements RegisterServiceInterface{
    UserRepository userRepository;
    UserRegisterValidator userRegisterValidator;
    RegisterService(UserRepository userRepository, UserRegisterValidator userRegisterValidator)
    {
        this.userRepository = userRepository;
        this.userRegisterValidator = userRegisterValidator;
    }
    @Async("MultiRequestAsyncThread")
    @Override
    public CompletableFuture<String> register(UserRegister userRegister){
        return CompletableFuture.supplyAsync(() -> {
            Errors errors = new BeanPropertyBindingResult(userRegister, "userLogin");
            userRegisterValidator.validate(userRegister, errors);
            if (errors.hasErrors()) {
                throw new InvalidInputException("Invalid login details");
            }
            UserRegisterDetails userRegisterDetails = new UserRegisterDetails();
            userRegisterDetails.setUsername(userRegister.getUsername());
            userRegisterDetails.setPassword(userRegister.getPassword());
            userRegisterDetails.setEmail(userRegister.getEmail());
            userRepository.save(userRegisterDetails);
            log.info("User Registered with ID: {}", userRegisterDetails.getId());
            log.info("User Registered: {}", userRegister.getUsername());
            return "Registered";
        });
    }
}
