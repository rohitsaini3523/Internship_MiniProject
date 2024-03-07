package com.example.Backend.services;

import com.example.Backend.model.UserLogin;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public interface LoginServiceInterface {
    public CompletableFuture<UserLogin> getUserDetails(String name);
    public CompletableFuture<String> login(UserLogin userLogin);

}
