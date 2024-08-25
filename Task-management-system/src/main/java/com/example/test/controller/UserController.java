package com.example.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.entity.User;
import com.example.test.security.AuthRequest;
import com.example.test.security.AuthResponse;
import com.example.test.security.JwtUtil;
import com.example.test.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		try {
			User registeredUser = userService.registerUser(user);
			return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser); // Return HTTP 201 Created
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest authRequest) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

			final UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
			final String jwt = jwtUtil.generateToken(userDetails.getUsername());

			return ResponseEntity.ok(new AuthResponse(jwt));
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Authentication failed: " + e.getMessage());
		}
	}
}
