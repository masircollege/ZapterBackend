package com.zapter.zapter_backend.product.service;

import com.zapter.zapter_backend.product.domain.*;
import com.zapter.zapter_backend.product.dto.KeyFeaturesDTO;
import com.zapter.zapter_backend.product.dto.prod.ProductAdd;
import com.zapter.zapter_backend.product.dto.prod.ProductResponse;
import com.zapter.zapter_backend.product.mapper.ProductMapper;
import com.zapter.zapter_backend.product.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;
    private final ProductKeyFeatureRepository productKeyFeatureRepository;

    public ProductService(
            ProductRepository productRepository,
            ProductMapper productMapper,
            CategoryRepository categoryRepository,
            BrandRepository brandRepository,
            ModelRepository modelRepository,
            ProductKeyFeatureRepository productKeyFeatureRepository
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
        this.productKeyFeatureRepository = productKeyFeatureRepository;
    }

    public List<ProductResponse> getProducts() {
        try {
            List<Product> products = productRepository.findAll();
            return products.stream()
                    .map(product -> new ProductResponse(
                            product.getId(),
                            product.getName(),
                            product.getCategory().getId(),
                            product.getBrand().getId(),
                            product.getModel().getId(),
                            product.getColor(),
                            productKeyFeatureRepository.getKeyFeaturesByProductId(product.getId()),
                            product.getDescription(),
                            product.getPrice(),
                            product.getStockStatus()
                    ))
                    .toList();
//            return productMapper.toListOfProductResponse(productRepository.findAll());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public void createProd(ProductAdd newProduct) {
        try {
            Category category = categoryRepository.findById(newProduct.categoryId()).orElseThrow();
            Brand brand = brandRepository.findById(newProduct.brandId()).orElseThrow();
            Model model = modelRepository.findById(newProduct.modelId()).orElseThrow();
            Product product = new Product();
            product.setCategory(category);
            product.setBrand(brand);
            product.setModel(model);
            product.setName(newProduct.name());
            product.setColor(newProduct.color());
            product.setDescription(newProduct.description());
            product.setPrice(newProduct.price());
            productRepository.save(product);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ProductResponse getProductById(Long id) {
        try {
            List<KeyFeaturesDTO> productKeyFeatures = productKeyFeatureRepository.getKeyFeaturesByProductId(id);
            Product product = productRepository.findById(id).orElseThrow();
            return new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getCategory().getId(),
                    product.getBrand().getId(),
                    product.getModel().getId(),
                    product.getColor(),
                    productKeyFeatures,
                    product.getDescription(),
                    product.getPrice(),
                    product.getStockStatus()
            );
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
