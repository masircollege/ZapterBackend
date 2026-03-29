package com.zapter.zapter_backend.user.mapper;

import com.zapter.zapter_backend.user.domain.Vendor;
import com.zapter.zapter_backend.user.dto.vendor.VendorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VendorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Vendor toVendor(VendorResponse newVendor);

//    @Mapping(target = "createdAt", ignore = true)
    List<VendorResponse> toListOfVendorDto(List<Vendor> vendor);

//    @Mapping(target = "createdAt", ignore = true)
    VendorResponse toListOfVendorDto(Vendor vendor);
}
