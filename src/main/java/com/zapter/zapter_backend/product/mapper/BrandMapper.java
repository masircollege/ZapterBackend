package com.zapter.zapter_backend.product.mapper;

import com.zapter.zapter_backend.product.domain.Brand;
import com.zapter.zapter_backend.product.dto.brand.BrandResponse;
import com.zapter.zapter_backend.product.dto.brand.NewBrand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Brand toBrand(NewBrand newBrand);

    @Mapping(target = "createdAt", ignore = true)
    List<BrandResponse> toListOfBrandResponse(List<Brand> brand);

    @Mapping(target = "createdAt", ignore = true)
    BrandResponse toBrandResponse(Brand brand);
}
