package com.example.Backend.services;

import com.example.Backend.entity.UserDetails;
import com.example.Backend.model.User;
import com.example.Backend.respository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    UserRepository userRepository;
    RegisterService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
    public void register(User user)
    {
        UserDetails userDetails = new UserDetails();
        userDetails.setId(1);
        userDetails.setUsername(user.getUsername());
        userDetails.setPassword(user.getPassword());
        userRepository.save(userDetails);
        System.out.println("User logged in is "+ user.getUsername());
    }
}
