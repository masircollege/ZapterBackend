package com.zapter.zapter_backend.product.controller;

import com.zapter.zapter_backend.product.dto.inventory.NewInventory;
import com.zapter.zapter_backend.product.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zapter/admin/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','INVENTORY_ADMIN')")
    @PostMapping("/new")
    public ResponseEntity<?> newInventory(@RequestBody NewInventory newInventory){
        try {
            inventoryService.createInventory(newInventory);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> getInventoryDetail(){
        try {
            return new ResponseEntity<>(inventoryService.get(),HttpStatus.OK);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInventoryById(@PathVariable Long id){
        try {
            return new ResponseEntity<>(inventoryService.getById(id),HttpStatus.OK);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }


    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','INVENTORY_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInventoryById(@PathVariable Long id){
        try {
            inventoryService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }
}
