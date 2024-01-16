package com.example.Backend.respository;

import com.example.Backend.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDetails,Integer> {
    UserDetails findByUsername(String name);
}
