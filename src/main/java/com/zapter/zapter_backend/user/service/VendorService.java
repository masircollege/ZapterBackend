package com.zapter.zapter_backend.user.service;

import com.zapter.zapter_backend.user.domain.Vendor;
import com.zapter.zapter_backend.user.dto.vendor.VendorResponse;
import com.zapter.zapter_backend.user.mapper.VendorMapper;
import com.zapter.zapter_backend.user.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    public VendorService(
            VendorRepository vendorRepository,
            VendorMapper vendorMapper
    ){
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
    }

    public void createVendor(VendorResponse vendorResponse){
        try {
            vendorRepository.save(vendorMapper.toVendor(vendorResponse));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Vendor vendor){
        vendorRepository.save(vendor);
    }

    public void delete(Long id){
        try {
            vendorRepository.deleteById(id);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    public List<VendorResponse> get(){
        try {
            return vendorMapper.toListOfVendorDto(vendorRepository.findAll());
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }
    public VendorResponse getById(Long id){
        try {
            return vendorMapper.toListOfVendorDto(vendorRepository.findById(id).orElseThrow(RuntimeException::new));
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }
}
