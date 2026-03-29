package com.zapter.zapter_backend.product.controller;

import com.zapter.zapter_backend.product.dto.prod.ProductAdd;
import com.zapter.zapter_backend.product.dto.prod.ProductResponse;
import com.zapter.zapter_backend.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zapter/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(){
        try {
            return new ResponseEntity<>(productService.getProducts(), HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductAdd newProduct){
        try {
            productService.createProd(newProduct);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
        try {
            return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
