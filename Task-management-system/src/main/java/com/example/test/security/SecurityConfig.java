package com.example.test.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.test.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtRequestFilter jwtRequestFilter;
	private final UserService userDetailsService;

	public SecurityConfig(@Lazy JwtRequestFilter jwtRequestFilter, UserService userDetailsService) {
		this.jwtRequestFilter = jwtRequestFilter;
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().requestMatchers("/register", "/login").permitAll() // Allow public
																										// access to
																										// registration
																										// and login
																										// endpoints
				.anyRequest().authenticated() // Require authentication for all other requests
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessions
				.and().exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
				});

		// Add JWT filter
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
