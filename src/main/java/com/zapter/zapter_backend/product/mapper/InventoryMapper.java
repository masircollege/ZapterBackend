package com.zapter.zapter_backend.product.mapper;

import com.zapter.zapter_backend.product.domain.Inventory;
import com.zapter.zapter_backend.product.dto.inventory.InventoryResponse;
import com.zapter.zapter_backend.product.dto.inventory.NewInventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
//    @Mapping(target = "id", ignore = true)  // Cannot Mapp directly as it'll require Product Instance..
//    @Mapping(target = "firstBatch", ignore = true)
//    @Mapping(target = "latestBatch", ignore = true)
//    Inventory toInventory(NewInventory newInventory);

//    List<InventoryResponse> toListOfInventoryResponse(List<Inventory> inventory);

//    InventoryResponse toInventoryResponse(Inventory inventory);
}
