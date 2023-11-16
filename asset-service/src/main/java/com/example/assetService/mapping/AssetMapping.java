package com.example.assetService.mapping;

import com.example.assetService.client.AssetServiceClient;
import com.example.assetService.dto.AssetResponse;
import com.example.assetService.model.Asset;
import com.example.assetService.model.AssetType;
import com.example.assetService.repository.AssetTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AssetMapping {
    private final AssetServiceClient assetServiceClient;
    private final AssetTypeRepository assetTypeRepository;
    public AssetResponse getAssetResponse(Asset asset) {
        AssetResponse assetResponse = new AssetResponse();
        assetResponse.setAssetId(asset.getAssetId());
        assetResponse.setAssetName(asset.getAssetName());
        assetResponse.setAssetTypeId(asset.getAssetType());
        Optional<AssetType> assetType = assetTypeRepository.findById(asset.getAssetType());
        if(assetType.isPresent()){
            assetResponse.setAssetTypeName(assetType.get().getAssetName());
            assetResponse.setAssetGroupId(assetType.get().getAssetGroup().getId());
            assetResponse.setAssetGroup(assetType.get().getAssetGroup().getName());
        }
        assetResponse.setPrice(asset.getPrice());
        assetResponse.setStatus(asset.getAssetStatus());
        switch (Math.toIntExact(asset.getAssetStatus())){
            case 0: assetResponse.setStatusName("Chưa sử dụng");break;
            case 1: assetResponse.setStatusName("Đang sử dụng");break;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assetResponse.setDateInStored(dateFormat.format(asset.getDateInStored()));
        assetResponse.setDateUsed(dateFormat.format(asset.getDateUsed()));
        assetResponse.setUserIdUsed(asset.getUserUsedId());
        assetResponse.setDeptIdUsed(asset.getDeptUsedId());
        assetResponse.setUser(assetServiceClient.fetchUser(Long.valueOf(asset.getAssetId())));
        return assetResponse;

    }
}
