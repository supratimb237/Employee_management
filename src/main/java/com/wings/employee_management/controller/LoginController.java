package com.wings.employee_management.controller;

import com.wings.employee_management.model.JwtResponse;
import com.wings.employee_management.model.LoginRequest;
import com.wings.employee_management.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationProvider authenticationProvider;

    @PostMapping("/auth/login")
    public ResponseEntity<?> doLogin(@RequestBody LoginRequest loginReq) {
        System.out.println("Username: "+loginReq.getUsername()+" Password: "+loginReq.getPassword());
        try {
            authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword()));
            String token = jwtService.generateToken(loginReq.getUsername());
            System.out.println("Token is: "+token);
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setToken(token);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(400).build();
        }
    }
}
