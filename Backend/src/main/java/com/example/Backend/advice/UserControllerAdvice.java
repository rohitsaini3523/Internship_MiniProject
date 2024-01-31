package com.example.Backend.advice;

import com.example.Backend.exceptions.InvalidInputException;
import com.example.Backend.exceptions.UserNotFoundException;
import com.example.Backend.model.ErrorResponse;
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
    ErrorResponse errorResponse = new ErrorResponse();

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
            UserNotFoundException ex) {
        errorResponse.setError("User Not Found");
        errorResponse.setMessage(ex.getMessage());
        log.error(" User not Found: {}",ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex) {
        errorResponse.setError("User Registration Error");
        errorResponse.setMessage("Already Exists!");
        log.error("Data integrity violation occurred during user registration: {}",ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException ex)
    {
        errorResponse.setError("Invalid Input Error");
        errorResponse.setMessage(ex.getMessage());
        log.error("Invalid Input Error: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
    {
        String message = "";
        errorResponse.setError("Not Valid Argument");
        message = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        errorResponse.setMessage(message);
        log.error("Not Valid Argument: {}", message);
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleException(Exception ex)
//    {
//        errorResponse.setError("Bad Request");
//        errorResponse.setMessage(ex.getMessage());
//        log.error("Bad Request\n Error: {}", ex.getMessage());
//        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
//    }
}