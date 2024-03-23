package com.example.Backend.services;

import com.example.Backend.entity.UserRegisterDetails;
import com.example.Backend.exceptions.InvalidInputException;
import com.example.Backend.exceptions.UserRegistrationException;
import com.example.Backend.model.UserRegister;
import com.example.Backend.respository.UserRepository;
import com.example.Backend.validator.UserRegisterValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {
    UserRepository userRepository;
    UserRegisterValidator userRegisterValidator;
    RegisterServiceImpl(UserRepository userRepository, UserRegisterValidator userRegisterValidator)
    {
        this.userRepository = userRepository;
        this.userRegisterValidator = userRegisterValidator;
    }

    @Override
    public String register(UserRegister userRegister)
    {
        Errors errors = new BeanPropertyBindingResult(userRegister, "userLogin");
        userRegisterValidator.validate(userRegister, errors);
        if(errors.hasErrors()) {
            throw new InvalidInputException("Invalid Register details");
        }
        if (userRepository.existsByUsername(userRegister.getUsername())) {
            throw new UserRegistrationException("User with username '" + userRegister.getUsername() + "' already exists");
        }

        UserRegisterDetails userRegisterDetails = new UserRegisterDetails();
        userRegisterDetails.setUsername(userRegister.getUsername());
        userRegisterDetails.setPassword(userRegister.getPassword());
        userRegisterDetails.setEmail(userRegister.getEmail());
        userRepository.save(userRegisterDetails);
        log.info("User Registered with ID: {}", userRegisterDetails.getId());
        log.info("User Registered: {}", userRegister.getUsername());
        return "Registered";
    }
}
