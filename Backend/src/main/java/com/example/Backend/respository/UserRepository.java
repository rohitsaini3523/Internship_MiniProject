package com.example.Backend.respository;

import com.example.Backend.entity.UserRegisterDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserRegisterDetails,Integer> {
    UserRegisterDetails findByUsername(String name);
    UserRegisterDetails findByPassword(String password);
    UserRegisterDetails findByUsernameAndPassword(String name, String password);
}
