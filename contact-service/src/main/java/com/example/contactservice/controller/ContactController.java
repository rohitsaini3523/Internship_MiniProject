package com.example.contactservice.controller;

import com.example.contactservice.exceptions.InvalidInputException;
import com.example.contactservice.exceptions.UserNotFoundException;
import com.example.contactservice.model.UserContact;
import com.example.contactservice.model.UserResponse;
import com.example.contactservice.services.ContactServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/contact/")
public class ContactController {
    ContactServiceInterface contactServiceInterface;
    UserResponse userResponse = new UserResponse();
    @Autowired
    RestTemplate restTemplate;
    ContactController(ContactServiceInterface contactServiceInterface) {

        this.contactServiceInterface = contactServiceInterface;
    }

    @Operation(summary = "Display user contact details", description = "Get user contact details by {username}/contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",description = "User Doesn't exists"),
            @ApiResponse(responseCode = "200", description = "User Fetched Successfully")
    })
    @GetMapping("/{name}")
    public CompletableFuture<ResponseEntity<?>> displayUserContact(@PathVariable(value = "name") String username) {
        CompletableFuture<List<UserContact>> userContactsFuture = this.contactServiceInterface.getContactDetails(username);
        return userContactsFuture.thenApply(userContacts -> {
            if (!userContacts.isEmpty()) {
                log.info("User Contact Details Fetched: {}",username);
                return new ResponseEntity<>(userContacts, HttpStatus.OK);
            } else {
                throw new UserNotFoundException("User Contact Details Doesn't Exists");
            }
        });
    }



    @Operation(summary = "user Contact Update", description = "Add user contact details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User Contact Details Added successfully!"),
            @ApiResponse(responseCode = "409", description = "User Contact Details Exists Already!"),
            @ApiResponse(responseCode = "401", description = "Insufficient Parameters!")
    })
    @PostMapping("/{name}/add")
    public ResponseEntity<?> addContactDetails(@PathVariable(value="name") String name, @RequestHeader(HttpHeaders.AUTHORIZATION) String token , @RequestBody @Valid UserContact userContact) throws ExecutionException, InterruptedException {
        token = extractTokenFromHeader(token);
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://AuthenticationService/auth/validate",token,String.class);
        System.out.println(response.toString());
        if (userContact.getUsername().equals(name) && response.toString().equals(name)) {
            CompletableFuture<String> addContactFuture = contactServiceInterface.addContactDetails(name, userContact);
            String result = addContactFuture.get();
            log.info("User Contact Detail Added!");
            userResponse.setMessage("User Contact Details Added!: " + userContact.getUsername());
            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        } else {
            throw new InvalidInputException("Invalid Request!");
        }
    }
    private String extractTokenFromHeader(String authorizationHeader) {
        String[] parts = authorizationHeader.split(" ");
        if (parts.length == 2 && parts[0].equalsIgnoreCase("Bearer")) {
            return parts[1];
        }
        return "No";
    }
}