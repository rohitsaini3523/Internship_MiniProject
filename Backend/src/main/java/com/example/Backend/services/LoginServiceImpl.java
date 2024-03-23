package com.example.Backend.services;

import com.example.Backend.entity.UserRegisterDetails;
import com.example.Backend.exceptions.InvalidInputException;
import com.example.Backend.exceptions.UserNotFoundException;
import com.example.Backend.model.UserLogin;
import com.example.Backend.respository.UserRepository;
import com.example.Backend.validator.UserLoginValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    UserRepository userRepository;
    UserLoginValidator userLoginValidator;
    LoginServiceImpl(UserRepository userRepository, UserLoginValidator userLoginValidator)
    {
        this.userRepository = userRepository;
        this.userLoginValidator = userLoginValidator;
    }

    @Override
    public UserLogin getUserDetails(String name)
    {
        UserRegisterDetails userRegisterDetails = userRepository.findByUsername(name);
        if(userRegisterDetails !=null) {
            return UserLogin.builder().username(userRegisterDetails.getUsername())
                    .password(userRegisterDetails.getPassword()).build();
        }
        throw new UserNotFoundException("User doesn't exists");
    }

    @Override
    public String login(UserLogin userLogin)
    {
        Errors errors = new BeanPropertyBindingResult(userLogin, "userLogin");
        userLoginValidator.validate(userLogin, errors);
        if(errors.hasErrors()) {
            throw new InvalidInputException("Invalid Input details");
        }
        UserRegisterDetails userRegisterDetails = userRepository.findByUsernameAndPassword(userLogin);
        UserRegisterDetails FindUsername = userRepository.findByUsername(userLogin.getUsername());
        if(FindUsername != null) {
            if (userRegisterDetails != null) {
                log.info("User Logged in is: {}!", userLogin.getUsername());
                return "Found";
            }
            throw new InvalidInputException("Wrong Password!");
        }
        throw new UserNotFoundException("User doesn't exists");
    }

}
