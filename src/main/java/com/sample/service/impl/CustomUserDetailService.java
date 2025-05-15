package com.sample.service.impl;

import com.sample.domain.User;
import com.sample.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation for loading user-specific data.
 * <p>
 * This class implements the Spring Security `UserDetailsService` interface
 * to provide user authentication and authorization details.
 * </p>
 *
 * @author Alagu Nirmal Mahendran
 * @created 2025-06-05
 */
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor to initialize the UserRepository.
     *
     * @param userRepository the repository for user persistence
     */
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by username.
     * Fetches the user from the database and maps their roles to granted authorities.
     *
     * @param username the username of the user to load
     * @return the UserDetails object containing user authentication details
     * @throws RuntimeException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
