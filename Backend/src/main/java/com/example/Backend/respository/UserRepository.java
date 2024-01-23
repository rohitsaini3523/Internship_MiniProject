package com.example.Backend.respository;

import com.example.Backend.entity.UserRegisterDetails;
import com.example.Backend.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserRegisterDetails,Integer> {
    UserRegisterDetails findByUsername(String name);
    UserRegisterDetails findByPassword(String password);
    @Query("SELECT user FROM UserRegisterDetails user WHERE user.username = :#{#userLogin.username} AND user.password = :#{#userLogin.password}")
    UserRegisterDetails findByUsernameAndPassword(UserLogin userLogin);
}
