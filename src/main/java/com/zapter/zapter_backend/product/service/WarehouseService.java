package com.zapter.zapter_backend.product.service;

import com.zapter.zapter_backend.product.domain.Warehouse;
import com.zapter.zapter_backend.product.dto.warehouse.WarehouseResponse;
import com.zapter.zapter_backend.product.dto.warehouse.NewWarehouse;
import com.zapter.zapter_backend.product.mapper.WarehouseMapper;
import com.zapter.zapter_backend.product.repository.WarehouseRepository;
import com.zapter.zapter_backend.user.mapper.VendorMapper;
import com.zapter.zapter_backend.user.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final VendorRepository vendorRepository;

    public WarehouseService(
            WarehouseRepository warehouseRepository,
            WarehouseMapper warehouseMapper,
            VendorRepository vendorRepository
            ){
        this.warehouseRepository = warehouseRepository;
        this.warehouseMapper = warehouseMapper;
        this.vendorRepository = vendorRepository;
    }

    public void createWarehouse(NewWarehouse newWarehouse){
        try {
            Warehouse warehouse = new Warehouse();
            warehouse.setName(newWarehouse.name());
            warehouse.setAddress(newWarehouse.address());
            warehouse.setVendor(vendorRepository.findById(newWarehouse.vendorId()).orElseThrow());
            warehouseRepository.save(warehouse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Warehouse warehouse){
        warehouseRepository.save(warehouse);
    }

    public void delete(Long id){
        try {
            warehouseRepository.deleteById(id);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    public List<WarehouseResponse> get(){
        try {
            List<Warehouse> warehouses = warehouseRepository.findAll();
            return warehouses.stream()
                    .map(warehouse -> new WarehouseResponse(
                            warehouse.getId(),
                            warehouse.getName(),
                            warehouse.getAddress(),
                            warehouse.getVendor().getId()
                    )).toList();
//            return warehouseMapper.toListOfWarehouseResponse(warehouseRepository.findAll());
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    public WarehouseResponse getById(Long id){
        try {
            Warehouse warehouse = warehouseRepository.findById(id).orElseThrow();

            return new WarehouseResponse(
                    warehouse.getId(),
                    warehouse.getName(),
                    warehouse.getAddress(),
                    warehouse.getVendor().getId()
            );
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }
}
