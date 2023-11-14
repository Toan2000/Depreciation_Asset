package com.example.produceService.controller;

import com.example.produceService.model.Asset;
import com.example.produceService.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private AssetService assetService;

    @PostMapping("/upload-assets-data")
    public ResponseEntity<?> uploadAssetsData(@RequestParam("file")MultipartFile file){
        this.assetService.saveAssetsToDatabase(file);
        return ResponseEntity
                .ok(Map.of("Message" , " Assets data uploaded and saved to database successfully"));
    }

    @GetMapping("")
    public ResponseEntity<List<Asset>> getAssets(){
        return new ResponseEntity<>(assetService.getAssets(), HttpStatus.FOUND);
    }
}
