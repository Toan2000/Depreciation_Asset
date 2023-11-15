package com.example.assetService.service.Impl;

import com.example.assetService.client.AssetServiceClient;
import com.example.assetService.dto.AssetResponse;
import com.example.assetService.dto.UserResponse;
import com.example.assetService.model.Asset;
import com.example.assetService.model.AssetType;
import com.example.assetService.repository.AssetRepository;
import com.example.assetService.repository.AssetTypeRepository;
import com.example.assetService.service.AssetService;
import com.example.assetService.service.ExcelUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;
    private final AssetServiceClient assetServiceClient;
    private final AssetTypeRepository assetTypeRepository;

    public void saveAssetsToDatabase(MultipartFile file){
        if(ExcelUploadService.isValidExcelFile(file)){
            try {
                List<Asset> assets = ExcelUploadService.getAssetsDataFromExcel(file.getInputStream());
                this.assetRepository.saveAll(assets);
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }
    }

    public List<Asset> getAssets(int page, int size, String sort){
        Pageable pageable = PageRequest.of(page,size,Sort.by(sort));
        return assetRepository.findAll(pageable).toList();
    }

    @Override
    public Asset findAssetById(Long id) {
        Optional<Asset> asset = assetRepository.findById(id);
        if(asset.isPresent())
            return asset.get();
        return null;
    }

    @Override
    public List<Asset> findAssetByDeptId(Long deptId, int page,int size,String sort){
        Pageable pageable = PageRequest.of(page,size, Sort.by(sort));
        Page<Asset> assetPage = assetRepository.findByDeptUsedId(deptId,pageable);
        return assetPage.toList();
    }

    @Override
    public List<Asset> findAssetByUserId(Long userId, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(sort));
        Page<Asset> assets = assetRepository.findByUserUsedId(userId,pageable);
        return assets.toList();
    }

    @Override
    public List<Asset> findAssetByAssetStatus(Long assetStatus, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(sort));
        Page<Asset> assets = assetRepository.findByAssetStatus(assetStatus,pageable);
        return assets.toList();
    }


    @Override
    public UserResponse getAssets1() {
        return assetServiceClient.fetchUser(Long.valueOf(1));
    }

    @Override
    public AssetResponse getAssetResponse(Asset asset) {
        AssetResponse assetResponse = new AssetResponse();
        assetResponse.setAssetId(asset.getAssetId());
        assetResponse.setAssetName(asset.getAssetName());
        assetResponse.setAssetTypeId(asset.getAssetType());
        Optional<AssetType> assetType = assetTypeRepository.findById(asset.getAssetType());
        if(assetType.isPresent())
            assetResponse.setAssetTypeName(assetType.get().getAssetName());
        assetResponse.setPrice(asset.getPrice());
        assetResponse.setStatus(asset.getAssetStatus());
        switch (Math.toIntExact(asset.getAssetStatus())){
            case 0: assetResponse.setStatusName("Chưa sử dụng");break;
            case 1: assetResponse.setStatusName("Đang sử dụng");break;
        }
        assetResponse.setDateInStored(asset.getDateInStored());
        assetResponse.setDateUsed(asset.getDateInStored());
        assetResponse.setUserIdUsed(asset.getUserUsedId());
        assetResponse.setDeptIdUsed(asset.getDeptUsedId());
        assetResponse.setUser(assetServiceClient.fetchUser(Long.valueOf(asset.getUserUsedId())));
        return assetResponse;

    }
}
