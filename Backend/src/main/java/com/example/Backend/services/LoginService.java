package com.example.Backend.services;

import com.example.Backend.entity.UserRegisterDetails;
import com.example.Backend.model.UserLogin;
import com.example.Backend.respository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginService {
    UserRepository userRepository;
    LoginService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
    public UserLogin getUserDetails(String name)
    {
        UserRegisterDetails userRegisterDetails = userRepository.findByUsername(name);
        return UserLogin.builder().username(userRegisterDetails.getUsername())
                .password(userRegisterDetails.getPassword()).build();
    }
    public boolean login(UserLogin userLogin)
    {
        UserRegisterDetails userRegisterDetails = userRepository.findByUsernameAndPassword(userLogin);
        if(userRegisterDetails != null)
        {
            log.info("User Logged in is: {}!",userLogin.getUsername());
            return true;
        }
        return false;
    }

}
