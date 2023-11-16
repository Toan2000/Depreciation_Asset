package com.example.assetService.service;

import com.example.assetService.dto.response.UserResponse;
import com.example.assetService.model.Asset;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
@Component
public interface AssetService {
    void saveAssetsToDatabase(MultipartFile file);
    Page<Asset> getAssets(int page, int size, String sort);
    Asset findAssetById(Long id);

    Page<Asset> findAssetByDeptId(Long deptId, int page, int size, String sort);
    Page<Asset> findAssetByUserId(Long userId, int page, int size, String sort);

    UserResponse getAssets1();
    Page<Asset> findAssetByAssetType(Long assetTypeId, int page, int size, String sort);
    Page<Asset> findAssetByAssetStatus(Long assetStatus, int page, int size, String sort);
    Page<Asset> findAssetByDate(Date fromDate, Date toDate, int page, int size, String sort);
    boolean createAsset(Asset asset);
}