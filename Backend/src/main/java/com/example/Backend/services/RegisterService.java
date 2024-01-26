package com.example.Backend.services;

import com.example.Backend.entity.UserRegisterDetails;
import com.example.Backend.model.UserRegister;
import com.example.Backend.respository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegisterService implements RegisterServiceInterface{
    UserRepository userRepository;
    RegisterService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
    public String register(UserRegister userRegister)
    {
        UserRegisterDetails userRegisterDetails = new UserRegisterDetails();
        userRegisterDetails.setUsername(userRegister.getUsername());
        userRegisterDetails.setPassword(userRegister.getPassword());
        userRepository.save(userRegisterDetails);
        log.info("User Registered with ID: {}", userRegisterDetails.getId());
        log.info("User Registered: {}", userRegister.getUsername());
        return "Registered";
    }
}
