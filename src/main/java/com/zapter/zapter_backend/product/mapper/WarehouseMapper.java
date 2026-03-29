package com.zapter.zapter_backend.product.mapper;

import com.zapter.zapter_backend.product.domain.Warehouse;
import com.zapter.zapter_backend.product.dto.warehouse.NewWarehouse;
import com.zapter.zapter_backend.product.dto.warehouse.WarehouseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "createdAt", ignore = true)
//    Warehouse toWarehouse(NewWarehouse newWarehouse);

//    List<WarehouseResponse> toListOfWarehouseResponse(List<Warehouse> warehouse);

//    WarehouseResponse toWarehouseResponse(Warehouse warehouse);

}
