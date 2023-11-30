package com.example.assetService.repository;

import com.example.assetService.model.AssetDelivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetDeliveryRepository extends JpaRepository<AssetDelivery, Long> {
    List<AssetDelivery> findByAssetIdAndStatus(Long assetId, int status);
    List<AssetDelivery> findByAssetIdAndDeliveryType(Long assetId,int deliveryType);
}
