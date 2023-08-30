package com.pomika.carwashbackend.controller;

import com.pomika.carwashbackend.dto.JwtRequestDto;
import com.pomika.carwashbackend.dto.JwtResponseDto;
import com.pomika.carwashbackend.service.impl.AuthenticationServiceImpl;
import com.pomika.carwashbackend.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private final AuthenticationServiceImpl authenticationService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationController(
            AuthenticationServiceImpl authenticationService,
            JwtTokenUtils jwtTokenUtils,
            AuthenticationManager authenticationManager
    ){
        this.authenticationService = authenticationService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody JwtRequestDto jwtRequestDto
    ){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequestDto.getPhoneNumber(),
                            jwtRequestDto.getPassword()
                    )
            );
        } catch (AuthenticationException e){
            return new ResponseEntity<>("Неправильный логин или пароль", HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = authenticationService.loadUserByUsername(jwtRequestDto.getPhoneNumber());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponseDto(token));
    }

}
