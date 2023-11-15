package com.example.assetService.service;

import com.example.assetService.model.AssetType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public interface AssetTypeService {
    List<AssetType> getAllAsset();
    AssetType findAssetTypeById(Long id);
}
