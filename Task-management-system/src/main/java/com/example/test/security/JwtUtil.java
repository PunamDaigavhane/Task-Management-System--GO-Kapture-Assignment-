package com.example.test.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

	// Ensure this key is kept secure and consistent
	private final String SECRET_KEY = "your-very-secure-secret-key"; // Consider loading from configuration

	// Extract username from token
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// Extract expiration date from token
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	// Extract specific claims from token
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// Extract all claims from token
	private Claims extractAllClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(SECRET_KEY) // Ensure this matches the signing key
					.parseClaimsJws(token).getBody();
		} catch (Exception e) {
			// Handle exceptions such as ExpiredJwtException, UnsupportedJwtException, etc.
			throw new RuntimeException("Invalid JWT token", e);
		}
	}

	// Check if token has expired
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	// Generate token
	public String generateToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Ensure this matches the key used for validation
				.compact();
	}

	// Validate token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
