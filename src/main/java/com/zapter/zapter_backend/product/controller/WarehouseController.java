package com.zapter.zapter_backend.product.controller;

import com.zapter.zapter_backend.product.dto.warehouse.NewWarehouse;
import com.zapter.zapter_backend.product.service.WarehouseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zapter/admin/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService){
        this.warehouseService = warehouseService;
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping("/new")
    public ResponseEntity<?> newWarehouse(@RequestBody NewWarehouse newWarehouse){
        try {
            warehouseService.createWarehouse(newWarehouse);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','SYSTEM_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getWarehouseDetail(){
        try {
            return new ResponseEntity<>(warehouseService.get(),HttpStatus.OK);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','SYSTEM_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getWarehouseById(@PathVariable Long id){
        try {
            return new ResponseEntity<>(warehouseService.getById(id),HttpStatus.OK);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWarehouseById(@PathVariable Long id){
        try {
            warehouseService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }
}
