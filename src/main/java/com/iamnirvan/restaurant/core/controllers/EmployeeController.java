package com.iamnirvan.restaurant.core.controllers;

import com.iamnirvan.restaurant.core.models.requests.employee.EmployeeRegisterRequest;
import com.iamnirvan.restaurant.core.models.requests.employee.EmployeeUpdateRequest;
import com.iamnirvan.restaurant.core.services.IEmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/employee")
public class EmployeeController {
    private final IEmployeeService employeeService;

    @GetMapping
    public ResponseEntity<?> getAllEmployees(@RequestParam(value = "id", required = false) Long id) {
        return ResponseEntity.ok(employeeService.getAllEmployees(id));
    }

    @PostMapping("/register")
    public ResponseEntity<?> createEmployee(@Valid @RequestBody EmployeeRegisterRequest request) {
        return ResponseEntity.ok(employeeService.registerEmployee(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable("id") Long id, @Valid @RequestBody EmployeeUpdateRequest request) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Long id) {
        return ResponseEntity.ok(employeeService.deleteEmployee(id));
    }
}
