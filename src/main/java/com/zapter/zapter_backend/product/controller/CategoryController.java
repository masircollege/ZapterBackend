package com.zapter.zapter_backend.product.controller;

import com.zapter.zapter_backend.product.dto.category.CategoryResponse;
import com.zapter.zapter_backend.product.dto.category.NewCategory;
import com.zapter.zapter_backend.product.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zapter/admin/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){this.categoryService = categoryService;}

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping("/new")
    public ResponseEntity<?> createCategory(@Valid @RequestBody NewCategory newCategory){
        try {
            categoryService.create(newCategory);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(){
        try {
            return new ResponseEntity<>(categoryService.get(),HttpStatus.OK);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id){
        try {
            return new ResponseEntity<>(categoryService.getById(id),HttpStatus.OK);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Long id){
        try {
            categoryService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }
}
