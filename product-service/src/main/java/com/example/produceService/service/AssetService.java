package com.example.produceService.service;

import com.example.produceService.model.Asset;
import com.example.produceService.repository.AssetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class AssetService {
    private AssetRepository assetRepository;

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

    public List<Asset> getAssets(){
        return assetRepository.findAll();
    }
}