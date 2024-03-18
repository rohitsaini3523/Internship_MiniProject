package com.example.Backend.repository;

import com.example.Backend.entity.UserRegisterDetails;
import com.example.Backend.respository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

//@ActiveProfiles("local")
@DataJpaTest
public class UserRepositoryTest {
    @Autowired private UserRepository testUserRepository;

    @Test
    public void findByUsername()
    {
        UserRegisterDetails expectedUser = new UserRegisterDetails(1L, "rs3523","Rohit@123","rohit@gmail.com","9568000766");
        testUserRepository.save(expectedUser);

        UserRegisterDetails result = testUserRepository.findByUsername("rs3523");

        assertThat(result).isEqualTo(expectedUser);

    }

}
