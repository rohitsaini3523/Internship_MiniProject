package com.example.Backend.services;

import com.example.Backend.model.UserContact;

public interface ContactServiceInterface {
    public UserContact getContactDetails(String name);
    public String addContactDetails(String name, UserContact userContact);
}
