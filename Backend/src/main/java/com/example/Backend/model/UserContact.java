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
public class UserContact {
    @Size(min=5,message = "Username must be minimum of 5 character!")
    @NotBlank(message = "Username is Required!")
    private String username;
    @Size(min = 10, message = "Phone number Should of length 10")
    @NotBlank(message = "Phone Number is required!")
    private String phoneNumber;

}
