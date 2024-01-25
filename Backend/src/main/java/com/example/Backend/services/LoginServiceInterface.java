package com.example.Backend.services;

import com.example.Backend.model.UserLogin;
import org.springframework.stereotype.Service;

@Service
public interface LoginServiceInterface {
    public UserLogin getUserDetails(String name);
    public String login(UserLogin userLogin);

}
