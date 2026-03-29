package com.zapter.zapter_backend.product.controller;

import com.zapter.zapter_backend.product.dto.brand.BrandResponse;
import com.zapter.zapter_backend.product.dto.brand.NewBrand;
import com.zapter.zapter_backend.product.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zapter/admin/brand")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService){
        this.brandService = brandService;
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping("/new")
    public ResponseEntity<?> createBrand(@Valid @RequestBody NewBrand newBrand){
        try {
            brandService.create(newBrand);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public ResponseEntity<List<BrandResponse>> getBrandDetails(){
        try {
            return new ResponseEntity<>(brandService.get(),HttpStatus.OK);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> getBrandById(@PathVariable Long id){
        try {
            return new ResponseEntity<>(brandService.getById(id),HttpStatus.OK);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrandById(@PathVariable Long id){
        try {
            brandService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }
}
