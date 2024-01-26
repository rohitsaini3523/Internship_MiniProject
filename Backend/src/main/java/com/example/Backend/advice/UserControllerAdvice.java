package com.example.Backend.advice;

import com.example.Backend.exceptions.InvalidInputException;
import com.example.Backend.exceptions.UserNotFoundException;
import com.example.Backend.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class UserControllerAdvice {
    Response response = new Response();

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Response> handleUserNotFoundException(
            UserNotFoundException ex) {
        response.setError("User Not Found");
        response.setMessage(ex.getMessage());
        log.error(" User not Found: {}",ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Response> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex) {
        response.setError("User Registration Error");
        response.setMessage("Username Already Exists!");
        log.error("Data integrity violation occurred during user registration: {}",ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Response> handleInvalidInputException( InvalidInputException ex)
    {
        response.setError("Invalid Input Error");
        response.setMessage(ex.getMessage());
        log.error("Invalid Input Error: {}", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleAllExceptions(Exception ex) {
        response.setError("Internal Server Error");
        response.setMessage(ex.getMessage());
        log.error("Internal Server Error: {}", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}