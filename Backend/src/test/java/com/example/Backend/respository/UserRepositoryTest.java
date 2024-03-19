package com.example.Backend.respository;

import com.example.Backend.entity.UserContactDetails;
import com.example.Backend.entity.UserRegisterDetails;
import com.example.Backend.model.UserLogin;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository testUserRepository;
    UserRegisterDetails expectedUser;
    UserContactDetails expectedUserContactDetails;

    @BeforeEach
    void setUp() {
        expectedUserContactDetails = UserContactDetails.builder()
                .username("rs3523")
                .phoneNumber("9568000766")
                .build();
        expectedUser = UserRegisterDetails.builder()
                .username("rs3523")
                .password("Rohit@123")
                .email("rohit@gmail.com")
                .userContactDetailsSet(Set.of(expectedUserContactDetails))
                .build();
        testUserRepository.save(expectedUser);
    }

    @AfterEach
    void tearDown() {
        testUserRepository.deleteAll();
    }

    @Test
    void findByUsername() {
        String username = "rs3523";
        expectedUser = testUserRepository.findByUsername(username);
        assertNotNull(expectedUser);
        UserRegisterDetails result = testUserRepository.findByUsername("rs3523");
        assertThat(result).isEqualTo(expectedUser);
    }

    @Test
    void findByPassword() {
        String password = "Rohit@123";
        expectedUser = testUserRepository.findByPassword(password);
        assertNotNull(expectedUser);
        UserRegisterDetails result = testUserRepository.findByPassword("Rohit@123");
        assertThat(result).isEqualTo(expectedUser);
    }

    @Test
    void findByUsernameAndPassword() {
        UserLogin userLogin = new UserLogin("rs3523", "Rohit@123");
        expectedUser = testUserRepository.findByUsernameAndPassword(userLogin);
        assertNotNull(expectedUser);
        UserRegisterDetails result = testUserRepository.findByUsernameAndPassword(UserLogin.builder().username("rs3523").password("Rohit@123").build());
        assertThat(result).isEqualTo(expectedUser);
    }
}
