package com.example.Backend.respository;

import com.example.Backend.entity.UserContactDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserContactRepository extends JpaRepository<UserContactDetails,Integer> {
    UserContactDetails findByUsername(String name);
}
