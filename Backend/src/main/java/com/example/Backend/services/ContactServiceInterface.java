package com.example.Backend.services;

import com.example.Backend.model.UserContact;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ContactServiceInterface {
    CompletableFuture<List<UserContact>> getContactDetails(String username);
    public CompletableFuture<String> addContactDetails(String name, UserContact userContact);
}
