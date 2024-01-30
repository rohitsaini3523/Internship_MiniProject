package com.example.Backend.respository;

import com.example.Backend.entity.UserContactDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserContactRepository extends JpaRepository<UserContactDetails,Integer> {
    UserContactDetails findByUsername(String name);
    List<UserContactDetails> findAllByUsername(String name);
}
