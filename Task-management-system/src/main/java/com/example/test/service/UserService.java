package com.example.test.service;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.test.entity.User;
import com.example.test.repository.UserRepository;
import com.example.test.security.CustomUserDetails;

@Service
public class UserService implements UserDetailsService {

	
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username); // Fetch user from database
    if (user == null) {
        throw new UsernameNotFoundException("User not found");
    }
    
    // Create and return CustomUserDetails instance
    return new CustomUserDetails(
        user.getId(), 
        user.getUsername(), 
        user.getPassword(), 
        user.getAuthorities() // Assuming your User entity has a method for authorities
    );}

	public User registerUser(User user) {
		if (userRepository.findByUsername(user.getUsername()) != null) {
			throw new IllegalArgumentException("User already exists with username: " + user.getUsername());
		}
		user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt the password before saving
		User savedUser = userRepository.save(user);
		//logger.info("Registered new user with username: {}", savedUser.getUsername());
		return savedUser;
	}

	public User findByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			
		}
		return user;
	}
}
