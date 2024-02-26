package com.example.AuthenticationService.controller;

import com.example.AuthenticationService.entity.RefreshToken;
import com.example.AuthenticationService.model.*;
import com.example.AuthenticationService.service.AuthService;
import com.example.AuthenticationService.service.jwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;
    @Autowired
    jwtService jwtService;
    UserResponse userResponse = new UserResponse();
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public JwtResponse AuthenticateAndGetToken(@RequestBody UserLogin userLogin){
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
            System.out.println(authentication);
            if(authentication != null) {
                RefreshToken refreshToken = authService.createRefreshToken(userLogin.getUsername());
                System.out.println("Authentication successful for user: " + userLogin.getUsername());
                return JwtResponse.builder()
                        .accessToken(jwtService.generateToken(userLogin.getUsername()))
                        .token(refreshToken.getToken()).build();
            } else {
                System.out.println("Authentication failed for user: " + userLogin.getUsername());
                throw new UsernameNotFoundException("Invalid username or password");
            }
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed for user: " + userLogin.getUsername() + ". Reason: " + e.getMessage());
            throw new UsernameNotFoundException("Invalid username or password");
        }

    }

    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return authService.findByToken(refreshTokenRequest.getToken())
                .map(authService::verifyExpiration)
                .map(RefreshToken::getUsername)
                .map(username -> {
                    String accessToken = jwtService.generateToken(username);
                    return JwtResponse.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.getToken()).build();
                }).orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));
    }

    // -/validate -> will be called by api-gateway
    @PostMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestBody String token) {
        boolean isValid = jwtService.validateTokenOnly(token);
        String username = jwtService.extractUsername(token);
        System.out.println("Given token: "+ token);
        System.out.println("Given token valid: "+ isValid);
        System.out.println("Given token username: "+ username);

        if (isValid) {
            return ResponseEntity.ok().body(username);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


}


