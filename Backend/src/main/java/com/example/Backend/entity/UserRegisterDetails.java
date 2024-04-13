package com.example.Backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.Set;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "User_Details_1", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"})
})
public class UserRegisterDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String username;

    @NotNull
    @Column(nullable = false)
    private String password;

    @NotNull
    @Column(nullable = false)
    private String email;

    @OneToMany(targetEntity = UserContactDetails.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private Set<UserContactDetails> userContactDetailsSet;


}
