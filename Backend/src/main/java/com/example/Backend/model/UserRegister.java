package com.example.Backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegister {
    @Size(min = 5, message = "Username must be minimum of 5 characters!")
    @NotBlank(message = "Username is required!")
    private String username;

    @Size(min = 8, message = "Password must be minimum of 8 characters!")
    @NotBlank(message = "Password is required!")
    private String password;

    @NotBlank(message = "Email is required!")
    private String email;
}
