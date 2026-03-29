package com.zapter.zapter_backend.product.mapper;

import com.zapter.zapter_backend.product.domain.Product;
import com.zapter.zapter_backend.product.dto.prod.ProductDto;
import com.zapter.zapter_backend.product.dto.prod.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

//	@Mapping(target = "id", ignore = true)
//	@Mapping(target = "createdAt", ignore = true)
//	@Mapping(target = "updatedAt", ignore = true)
//	@Mapping(target = "stockStatus", ignore = true)
//	Product toProduct(ProductDto newProduct);

//	List<ProductResponse> toListOfProductResponse(List<Product> product);

//	ProductResponse toProductResponse(Product product);

}
