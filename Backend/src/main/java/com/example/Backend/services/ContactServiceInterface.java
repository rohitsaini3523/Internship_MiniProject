package com.example.Backend.services;

import com.example.Backend.model.UserContact;

import java.util.List;

public interface ContactServiceInterface {
    List<UserContact> getContactDetails(String username);
    public String addContactDetails(String name, UserContact userContact);
}
