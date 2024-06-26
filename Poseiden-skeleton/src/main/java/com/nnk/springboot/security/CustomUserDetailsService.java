package com.nnk.springboot.security;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds the user in database by username and returns a spring security user if the password is correct
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUserName(username)
                .orElseThrow(() -> {
                    logger.error("Failed to find user with username: " + username);
                    return new UsernameNotFoundException("User not found with username: " + username);
                });
        logger.info("Logging in user: " + user.getUsername());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getGrantedAuthorities(user.getRole()));
    }

    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }
}
