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
        if(userRegisterDetails !=null) {
            return UserLogin.builder().username(userRegisterDetails.getUsername())
                    .password(userRegisterDetails.getPassword()).build();
        }
        else{
            return UserLogin.builder().username("Not_Found").password("Not_Found").build();
        }
    }
    public String login(UserLogin userLogin)
    {
        UserRegisterDetails userRegisterDetails = userRepository.findByUsernameAndPassword(userLogin);
        UserRegisterDetails FindUsername = userRepository.findByUsername(userLogin.getUsername());
        if(FindUsername != null) {
            if (userRegisterDetails != null) {
                log.info("User Logged in is: {}!", userLogin.getUsername());
                return "Found";
            }
            else{
                return "Wrong Password";
            }
        }
        return "Not Found";
    }

}
