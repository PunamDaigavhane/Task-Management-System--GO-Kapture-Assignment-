package com.example.test.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.test.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
 public User findByUsername(String username);
 
}
