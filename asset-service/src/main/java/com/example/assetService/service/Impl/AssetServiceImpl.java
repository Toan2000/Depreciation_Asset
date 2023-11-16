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
import java.text.SimpleDateFormat;
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

    public Page<Asset> getAssets(int page, int size, String sort){
        Pageable pageable = PageRequest.of(page,size,Sort.by(sort));
        return assetRepository.findAll(pageable);
    }

    @Override
    public Asset findAssetById(Long id) {
        Optional<Asset> asset = assetRepository.findById(id);
        if(asset.isPresent())
            return asset.get();
        return null;
    }

    @Override
    public Page<Asset> findAssetByDeptId(Long deptId, int page,int size,String sort){
        Pageable pageable = PageRequest.of(page,size, Sort.by(sort));
        return assetRepository.findByDeptUsedId(deptId,pageable);
    }

    @Override
    public Page<Asset> findAssetByUserId(Long userId, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(sort));
        return assetRepository.findByUserUsedId(userId,pageable);
    }

    @Override
    public Page<Asset> findAssetByAssetStatus(Long assetStatus, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(sort));
       return assetRepository.findByAssetStatus(assetStatus,pageable);
    }


    @Override
    public UserResponse getAssets1() {
        return assetServiceClient.fetchUser(Long.valueOf(1));
    }
}
