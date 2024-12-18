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
@RequestMapping("/v1/account")
public class LoginController {
    private final AccountService accountService;

    @PostMapping("/customer/login")
    public ResponseEntity<?> customerLogin(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(accountService.customerLogin(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/employee/login")
    public ResponseEntity<?> employeeLogin(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(accountService.employeeLogin(loginRequest), HttpStatus.OK);
    }
}
