package com.example.contactservice.services;

import com.example.contactservice.entity.UserContactDetails;
import com.example.contactservice.exceptions.InvalidInputException;
import com.example.contactservice.exceptions.UserNotFoundException;
import com.example.contactservice.model.UserContact;
import com.example.contactservice.model.UserRegister;
import com.example.contactservice.respository.UserContactRepository;
import com.example.contactservice.validator.UserContactValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContactService implements ContactServiceInterface {

    @Autowired
    private RestTemplate restTemplate;
    UserContactRepository userContactRepository;
    UserContactValidator userContactValidator;

    ContactService(UserContactRepository userContactRepository, UserContactValidator userContactValidator)
    {
        this.userContactRepository = userContactRepository;
        this.userContactValidator = userContactValidator;
    }
    @Async("MultiRequestAsyncThread")
    @Override
    public CompletableFuture<List<UserContact>> getContactDetails(String name) {
        return CompletableFuture.supplyAsync(() -> {
            List<UserContactDetails> userContactDetailsList = userContactRepository.findAllByUsername(name);
            if (!userContactDetailsList.isEmpty()) {
                return userContactDetailsList.stream()
                        .map(contactDetails -> UserContact.builder()
                                .username(contactDetails.getUsername())
                                .phoneNumber(contactDetails.getPhoneNumber())
                                .build())
                        .collect(Collectors.toList());
            }
            throw new UserNotFoundException("User Contacts don't exist for username: " + name);
        });
    }

    @Async("MultiRequestAsyncThread")
    @Override
    public CompletableFuture<String> addContactDetails(String name, UserContact userContact) {
        return CompletableFuture.supplyAsync(() -> {
            Errors errors = new BeanPropertyBindingResult(userContact, "userLogin");
            userContactValidator.validate(userContact, errors);
            if (errors.hasErrors()) {
                throw new InvalidInputException("Invalid Contact details");
            }
            UserRegister findUser = this.restTemplate.getForObject("http://backend/user/" + name, UserRegister.class);
            if (findUser == null) {
                throw new UserNotFoundException("User with " + userContact.getUsername() + " username doesn't exist");
            }
            UserContactDetails userContactDetails = new UserContactDetails();
            userContactDetails.setUsername(userContact.getUsername());
            userContactDetails.setPhoneNumber(userContact.getPhoneNumber());
            userContactRepository.save(userContactDetails);
            log.info("User Contact Details Added with ID: {}", userContactDetails.getId());
            log.info("Username: {}", userContactDetails.getUsername());
            return "Added";
        });
    }
}
