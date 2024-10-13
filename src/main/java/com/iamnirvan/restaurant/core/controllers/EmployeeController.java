package com.iamnirvan.restaurant.core.controllers;

import com.iamnirvan.restaurant.core.services.IEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/employee")
public class EmployeeController {
    private final IEmployeeService employeeService;
//
//    @GetMapping
//    public ResponseEntity<?> getAllEmployees(@RequestParam(value = "id", required = false) Long id) {
//        return ResponseEntity.ok(employeeService.getAllEmployees(id));
//    }
//
//    @PostMapping
//    public ResponseEntity<?> createEmployee(@Valid @RequestBody EmployeeCreateRequest request) {
//        return ResponseEntity.ok(employeeService.createEmployee(request));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateEmployee(@PathVariable("id") Long id, @Valid @RequestBody EmployeeUpdateRequest request) {
//        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Long id) {
//        return ResponseEntity.ok(employeeService.deleteEmployee(id));
//    }
}
