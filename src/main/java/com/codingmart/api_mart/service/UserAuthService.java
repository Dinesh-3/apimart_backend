
package com.codingmart.api_mart.service;

import com.codingmart.api_mart.model.User;
import com.codingmart.api_mart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserAuthService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        System.out.println("email = " + email);
        User user = userRepository.findByEmail(email);
//        System.out.println("user = " + user.getEmail());
        if (user == null) {
            throw new UsernameNotFoundException("Email " + email + " not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>()
        );
    }

    private Collection<GrantedAuthority> getGrantedAuthority(User user) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        return authorities;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }
}