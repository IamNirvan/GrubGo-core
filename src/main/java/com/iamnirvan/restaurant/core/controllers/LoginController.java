package com.iamnirvan.restaurant.core.controllers;

import com.iamnirvan.restaurant.core.models.requests.login.LoginRequest;
import com.iamnirvan.restaurant.core.services.impl.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/login")
public class LoginController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(accountService.login(loginRequest), HttpStatus.OK);

    }
}
