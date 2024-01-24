package com.example.Backend.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegister {
    @NotBlank(message = "Username is Required!")
    private String username;
    @NotBlank(message = "Password is Required!")
    private String password;
}
