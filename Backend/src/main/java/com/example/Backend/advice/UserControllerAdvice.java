package com.example.Backend.advice;

import com.example.Backend.exceptions.InvalidInputException;
import com.example.Backend.exceptions.UserNotFoundException;
import com.example.Backend.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

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
        response.setMessage("Already Exists!");
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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
    {
        String message = "";
        response.setError("Not Valid Argument");
        message = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        response.setMessage(message);
        log.error("Not Valid Argument: {}", message);
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleException( Exception ex)
    {
        response.setError("Bad Request");
        response.setMessage(ex.getMessage());
        log.error("Bad Request\n Error: {}", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}