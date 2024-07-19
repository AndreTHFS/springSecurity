package com.twitter.springsecurity.controller;


import com.twitter.springsecurity.dto.LoginRequest;
import com.twitter.springsecurity.dto.LoginResponse;
import com.twitter.springsecurity.entities.Role;
import com.twitter.springsecurity.repository.UserRepository;
import com.twitter.springsecurity.service.TokenService;
import com.twitter.springsecurity.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class TokenController {

    private final TokenService tokenService;

    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder passwordEncoder;

    public TokenController(TokenService tokenService,  JwtEncoder jwtEncoder, BCryptPasswordEncoder passwordEncoder) {
        this.tokenService=tokenService;
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){

        var token = tokenService.login(loginRequest);

        return ResponseEntity.ok(token);


    }

}
