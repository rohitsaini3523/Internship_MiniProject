package com.example.Backend.controller;

import com.example.Backend.exceptions.InvalidInputException;
import com.example.Backend.exceptions.UserNotFoundException;
import com.example.Backend.model.UserContact;
import com.example.Backend.model.UserLogin;
import com.example.Backend.model.UserRegister;
import com.example.Backend.services.ContactServiceImpl;
import com.example.Backend.services.LoginServiceImpl;
import com.example.Backend.services.RegisterServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    RegisterServiceImpl registerService;
    @Mock
    LoginServiceImpl loginService;
    @Mock
    ContactServiceImpl contactService;
    @InjectMocks
    UserController userController;

    @Test
    void itShould_DisplayUser() {
        UserLogin userLogin = new UserLogin();
        userLogin.setUsername("rs3523");
        userLogin.setPassword("Rohit@123");
        when(loginService.getUserDetails(userLogin.getUsername())).thenReturn(userLogin);
        ResponseEntity<?> responseEntity = userController.displayUser("rs3523");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userLogin, responseEntity.getBody());
    }

    @Test
    void itShould_DisplayUserContact() {
        UserContact userContact1 = new UserContact("rs3523","9568000766");
        UserContact userContact2 = new UserContact("rs3523","9999977777");
        List<UserContact> expectedUserContactsList = new ArrayList<>();
        expectedUserContactsList.add(userContact1);
        expectedUserContactsList.add(userContact2);
        when(contactService.getContactDetails("rs3523")).thenReturn(expectedUserContactsList);
        ResponseEntity<?> result = userController.displayUserContact("rs3523");
        log.info("Result: "+ result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(result,new ResponseEntity<>(expectedUserContactsList,HttpStatus.OK));
    }

    @Test
    void itShould_LoginUser() {
        UserLogin userLogin = new UserLogin();
        userLogin.setUsername("rs3523");
        userLogin.setPassword("Rohit@123");
        ResponseEntity<?> result = userController.loginUser(userLogin);
        log.info("Logged in : "+ result);
        assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
    }

    @Test
    void itShould_RegisterUser() {
        UserRegister userRegister = new UserRegister();
        userRegister.setUsername("rs3523");
        userRegister.setPassword("Rohit@123");
        userRegister.setEmail("rohitsaini@gmail.com");
        ResponseEntity<?> result = userController.registerUser(userRegister);
        log.info("Register user: "+ result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void itShould_AddContactDetails() {
        UserContact userContact = new UserContact();
        userContact.setUsername("rs3523");
        userContact.setPhoneNumber("9988007722");
        ResponseEntity<?> result = userController.addContactDetails("rs3523", userContact);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void itShouldThrow_InvalidInputExceptionFor_AddContactDetails() {
        UserContact userContact = new UserContact();
        userContact.setUsername("invalidUsername");
        userContact.setPhoneNumber("0000000000");
        when(contactService.addContactDetails("invalidUsernam",userContact)).thenThrow(new InvalidInputException("Invalid Request"));
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            contactService.addContactDetails("invalidUsernam",userContact);
        });
        assertEquals("Invalid Request", exception.getMessage());
    }

    @Test
    void itShouldThrow_UserNotFoundException_ForGetUserDetails()
    {
        UserLogin userLogin = new UserLogin();
        userLogin.setUsername("nouser");
        when(loginService.getUserDetails(userLogin.getUsername())).thenThrow(new UserNotFoundException("User doesn't exists"));
        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            loginService.getUserDetails("nouser");
        });
        assertEquals("User doesn't exists", exception.getMessage());
    }
}