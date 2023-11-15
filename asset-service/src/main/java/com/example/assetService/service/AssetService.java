package com.example.assetService.service;

import com.example.assetService.dto.AssetResponse;
import com.example.assetService.dto.UserResponse;
import com.example.assetService.model.Asset;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Component
public interface AssetService {
    void saveAssetsToDatabase(MultipartFile file);
    List<Asset> getAssets(int page, int size, String sort);
    Asset findAssetById(Long id);

    List<Asset> findAssetByDeptId(Long deptId, int page, int size, String sort);
    List<Asset> findAssetByUserId(Long userId, int page, int size, String sort);

    UserResponse getAssets1();

    AssetResponse getAssetResponse(Asset asset);
}