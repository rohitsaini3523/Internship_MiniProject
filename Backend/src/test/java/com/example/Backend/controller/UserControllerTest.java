package com.example.Backend.controller;

import com.example.Backend.services.ContactServiceImpl;
import com.example.Backend.services.LoginServiceImpl;
import com.example.Backend.services.RegisterServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Mock
    private RegisterServiceImpl registerService;
    @Mock
    private LoginServiceImpl loginService;
    @Mock
    private ContactServiceImpl contactService;
    @InjectMocks
    private UserController userController;


    @BeforeEach
    void setUp() {
        userController = new UserController(registerService, loginService, contactService);
        MockitoAnnotations.openMocks(this);
    }


    @AfterEach
    void tearDown() {
    }
}