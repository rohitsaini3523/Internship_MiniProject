package com.example.Backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "User_Contact_Details_1", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"phoneNumber"})
})
public class UserContactDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(nullable = false,updatable = false)
    private String username;
    @NotNull
    @Column(nullable = false)
    private String phoneNumber;

}
