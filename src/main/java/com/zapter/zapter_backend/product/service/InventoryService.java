package com.zapter.zapter_backend.product.service;

import com.zapter.zapter_backend.product.domain.Inventory;
import com.zapter.zapter_backend.product.domain.Product;
import com.zapter.zapter_backend.product.domain.Warehouse;
import com.zapter.zapter_backend.product.dto.inventory.InventoryResponse;
import com.zapter.zapter_backend.product.dto.inventory.NewInventory;
import com.zapter.zapter_backend.product.mapper.InventoryMapper;
import com.zapter.zapter_backend.product.repository.InventoryRepository;
import com.zapter.zapter_backend.product.repository.ProductRepository;
import com.zapter.zapter_backend.product.repository.WarehouseRepository;
import com.zapter.zapter_backend.user.domain.Vendor;
import com.zapter.zapter_backend.user.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final ProductRepository productRepository;
    private final WarehouseRepository  warehouseRepository;
    private final VendorRepository vendorRepository;

    public InventoryService(
            InventoryRepository inventoryRepository,
            InventoryMapper inventoryMapper,
            ProductRepository productRepository,
            WarehouseRepository warehouseRepository,
            VendorRepository vendorRepository
    ){
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.vendorRepository = vendorRepository;
    }

    public void createInventory(NewInventory newInventory){
        try {
            Product product = productRepository.findById(newInventory.productId()).orElseThrow();
            Warehouse warehouse = warehouseRepository.findById(newInventory.warehouseId()).orElseThrow();
            Vendor vendor = vendorRepository.findById(newInventory.vendorId()).orElseThrow();

            Inventory inventory = new Inventory();
            inventory.setInventoryProduct(product);
            inventory.setInventoryWarehouse(warehouse);
            inventory.setInventoryVendor(vendor);
            inventory.setQuantity(newInventory.quantity());
            inventory.setMinimumCount(newInventory.minimumCount());
            inventoryRepository.save(inventory);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Inventory inventory){
        inventoryRepository.save(inventory);
    }

    public void delete(Long id){
        try {
            inventoryRepository.deleteById(id);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    public List<InventoryResponse> get(){
        try {
            List<Inventory> inventories = inventoryRepository.findAll();
            return inventories.stream()
                    .map(inventory -> new InventoryResponse(
                            inventory.getId(),
                            inventory.getQuantity(),
                            inventory.getMinimumCount(),
                            inventory.getInventoryProduct().getId(),
                            inventory.getInventoryWarehouse().getId(),
                            inventory.getInventoryVendor().getId()
                    ))
                    .toList();
//            return inventoryMapper.toListOfInventoryResponse(inventoryRepository.findAll());
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }
    public InventoryResponse getById(Long id){
        try {
            Inventory inventory = inventoryRepository.findById(id).orElseThrow();
            return new InventoryResponse(
                    inventory.getId(),
                    inventory.getQuantity(),
                    inventory.getMinimumCount(),
                    inventory.getInventoryProduct().getId(),
                    inventory.getInventoryWarehouse().getId(),
                    inventory.getInventoryVendor().getId()
            );
//            return inventoryMapper.toInventoryResponse(inventoryRepository.findById(id).orElseThrow(RuntimeException::new));
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }
}
