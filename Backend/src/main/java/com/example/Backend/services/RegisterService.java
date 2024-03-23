package com.example.Backend.services;

import com.example.Backend.model.UserRegister;
import org.springframework.stereotype.Service;

@Service
public interface RegisterService {
    public String register(UserRegister userRegister);
}
