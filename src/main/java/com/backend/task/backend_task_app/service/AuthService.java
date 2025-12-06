package com.backend.task.backend_task_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.backend.task.backend_task_app.dto.JwtRequest;
import com.backend.task.backend_task_app.dto.JwtResponse;
import com.backend.task.backend_task_app.jwt.JwtAuthHelper;

@Service
public class AuthService {

    @Autowired
    private JwtAuthHelper jwtHelper;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    public JwtResponse login(JwtRequest jwtRequest) {
        // Authenticate using AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                jwtRequest.getUsername(), 
                jwtRequest.getPassword()
            )
        );
        
        // Load UserDetails for token generation
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        
        // Generate token
        String token = jwtHelper.generateToken(userDetails);
        
        return new JwtResponse(token);
    }
}