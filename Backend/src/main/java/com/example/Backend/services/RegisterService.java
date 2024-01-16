package com.example.Backend.services;

import com.example.Backend.entity.UserRegisterDetails;
import com.example.Backend.model.UserRegister;
import com.example.Backend.respository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    UserRepository userRepository;
    RegisterService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
    public void register(UserRegister userRegister)
    {
        UserRegisterDetails userRegisterDetails = new UserRegisterDetails();
        userRegisterDetails.setId(1);
        userRegisterDetails.setUsername(userRegister.getUsername());
        userRegisterDetails.setPassword(userRegister.getPassword());
        userRepository.save(userRegisterDetails);
        System.out.println("User Registered is: "+ userRegister.getUsername());
    }
}
