package com.iamnirvan.restaurant.core.controllers;

import com.iamnirvan.restaurant.core.services.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/category")
public class CategoryController {
    private final ICategoryService categoryService;

//    @GetMapping
//    public ResponseEntity<?> getPortions(@RequestParam(value = "id", required = false) Long id) {
//        return new ResponseEntity<>(categoryService.getCategories(id), HttpStatus.OK);
//    }
//
//    @PostMapping
//    public ResponseEntity<?> createPortion(@Valid @RequestBody List<CategoryCreateRequest> requests) {
//        return new ResponseEntity<>(categoryService.createCategory(requests), HttpStatus.CREATED);
//    }
//
//    @PutMapping
//    public ResponseEntity<?> updateCustomer(@Valid @RequestBody List<CategoryUpdateRequest> requests) {
//        return new ResponseEntity<>(categoryService.updateCategory(requests), HttpStatus.OK);
//    }
//
//    @DeleteMapping
//    public ResponseEntity<?> deleteCustomer(@RequestParam(value = "ids") List<Long> ids) {
//        return new ResponseEntity<>(categoryService.deleteCategory(ids), HttpStatus.OK);
//    }
}
