package com.example.Backend.services;

import com.example.Backend.model.UserContact;

import java.util.List;

public interface ContactService {
    List<UserContact> getContactDetails(String username);
    public String addContactDetails(String name, UserContact userContact);
}
