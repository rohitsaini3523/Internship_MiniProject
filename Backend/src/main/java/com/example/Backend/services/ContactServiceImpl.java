package com.example.Backend.services;

import com.example.Backend.entity.UserContactDetails;
import com.example.Backend.entity.UserRegisterDetails;
import com.example.Backend.exceptions.InvalidInputException;
import com.example.Backend.exceptions.UserNotFoundException;
import com.example.Backend.model.UserContact;
import com.example.Backend.respository.UserContactRepository;
import com.example.Backend.respository.UserRepository;
import com.example.Backend.validator.UserContactValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContactServiceImpl implements ContactService {

    UserContactRepository userContactRepository;
    UserRepository userRepository;
    UserContactValidator userContactValidator;

    ContactServiceImpl(UserContactRepository userContactRepository, UserRepository userRepository, UserContactValidator userContactValidator)
    {
        this.userContactRepository = userContactRepository;
        this.userRepository = userRepository;
        this.userContactValidator = userContactValidator;
    }

    @Override
    public List<UserContact> getContactDetails(String name) {
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
    }

    @Override
    public String addContactDetails(String name, UserContact userContact) {
        Errors errors = new BeanPropertyBindingResult(userContact, "userLogin");
        userContactValidator.validate(userContact, errors);
        if(errors.hasErrors()) {
            throw new InvalidInputException("Invalid Contact details");
        }
        UserRegisterDetails findUser = userRepository.findByUsername(userContact.getUsername());
        if(findUser == null)
        {
            throw new UserNotFoundException("User with "+userContact.getUsername() +" username doesn't exists");
        }
        UserContactDetails userContactDetails = new UserContactDetails();
        userContactDetails.setUsername(userContact.getUsername());
        userContactDetails.setPhoneNumber(userContact.getPhoneNumber());
        userContactRepository.save(userContactDetails);
        log.info("User Contact Details Added with ID: {}", userContactDetails.getId());
        log.info("Username: {}", userContactDetails.getUsername());
        return "Added";
    }
}
