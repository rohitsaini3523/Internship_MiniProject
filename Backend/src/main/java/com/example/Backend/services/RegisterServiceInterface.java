package com.example.Backend.services;

import com.example.Backend.model.UserRegister;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public interface RegisterServiceInterface {
    public CompletableFuture<String> register(UserRegister userRegister);
}
