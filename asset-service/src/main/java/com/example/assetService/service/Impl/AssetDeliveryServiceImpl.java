package com.example.assetService.service.Impl;

import com.example.assetService.model.AssetDelivery;
import com.example.assetService.repository.AssetDeliveryRepository;
import com.example.assetService.service.AssetDeliveryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class AssetDeliveryServiceImpl implements AssetDeliveryService {
    private final AssetDeliveryRepository assetDeliveryRepository;
    @Override
    public List<AssetDelivery> findByAssetIdAndStatus(Long assetId, int status) {
        return assetDeliveryRepository.findByAssetIdAndStatus(assetId,status);
    }

    @Override
    public List<AssetDelivery> findByAssetIdAndDeliveryType(Long assetId, int deliveryType) {
        return assetDeliveryRepository.findByAssetIdAndDeliveryType(assetId,deliveryType);
    }

    @Override
    public List<AssetDelivery> findByAssetIdAndDeliveryType(Long assetId) {
        return assetDeliveryRepository.findByAssetIdAndDeliveryType(assetId);
    }

    @Override
    public void createDelivery(AssetDelivery assetDelivery) {
        assetDeliveryRepository.save(assetDelivery);
    }
}
