package com.zapter.zapter_backend.product.repository;

import com.zapter.zapter_backend.product.domain.ProductKeyFeature;
import com.zapter.zapter_backend.product.dto.KeyFeaturesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ProductKeyFeatureRepository extends JpaRepository<ProductKeyFeature, Long> {

    @Query(
            value =
                    "SELECT kf.id AS feature_id,kf.feature_name,pkf.value " +
                            "FROM key_features kf INNER JOIN product_key_feature pkf " +
                            "ON kf.id = pkf.feature_id WHERE pkf.product_id = :productId",
            nativeQuery = true)
    List<KeyFeaturesDTO> getKeyFeaturesByProductId(@Param("productId") Long productId);

}
