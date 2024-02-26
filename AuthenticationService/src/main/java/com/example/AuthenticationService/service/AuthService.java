package com.example.AuthenticationService.service;

import com.example.AuthenticationService.entity.RefreshToken;
import com.example.AuthenticationService.model.UserLogin;
import com.example.AuthenticationService.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    jwtService jwtService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    public RefreshToken createRefreshToken(String username){
        UserLogin user = this.restTemplate.getForObject("http://backend/user/" + username, UserLogin.class);
        RefreshToken refreshToken = RefreshToken.builder()
                .username(user.getUsername())
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(60000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }



    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;

    }
}
