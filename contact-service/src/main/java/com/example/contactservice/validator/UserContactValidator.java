package com.example.contactservice.validator;

import com.example.contactservice.model.UserContact;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserContactValidator implements Validator {

    @Override
    public boolean supports(Class<?> c) {
        return UserContact.class.equals(c);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"username","Username.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"phoneNumber","Password.empty");
        UserContact userContact = (UserContact) target;

    }
}
