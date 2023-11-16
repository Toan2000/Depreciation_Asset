package com.example.assetService.controller;

import com.example.assetService.dto.AssetResponse;
import com.example.assetService.dto.Response;
import com.example.assetService.mapping.AssetMapping;
import com.example.assetService.model.Asset;
import com.example.assetService.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/asset")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3001")

public class AssetController {
    private final AssetService assetService;
    private final AssetMapping assetMapping;
    @GetMapping("")
    public ResponseEntity<Response> getAllAsset(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "assetId") String sort){
        Map<String,Object> data = new HashMap<>();
        List<AssetResponse> assetResponses = new ArrayList<>();
        Page<Asset> assets = assetService.getAssets(page,size,sort);
        for(Asset asset: assets) assetResponses.add(assetMapping.getAssetResponse(asset));
        data.put("assets",assetResponses);
        data.put("totalPage",assets.getTotalPages());
        return new ResponseEntity<>(new Response("Danh sách tài sản",data),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Response> getAssetById(@PathVariable Long id){
        Map<String,Object> data = new HashMap<>();
        data.put("asset",assetMapping.getAssetResponse(assetService.findAssetById(id)));
        return new ResponseEntity<>(new Response("Thông tin tài sản",data),HttpStatus.OK);
    }
    @GetMapping("/dept/{id}")
    public ResponseEntity<Response> getAssetByDeptId(@PathVariable Long id,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "assetId") String sort){
        Map<String,Object> data = new HashMap<>();
        Page<Asset> assets = assetService.findAssetByDeptId(id,page,size,sort);
        List<AssetResponse> assetResponses = new ArrayList<>();
        for(Asset asset: assets) assetResponses.add(assetMapping.getAssetResponse(asset));
        data.put("assets",assetResponses);
        data.put("totalPage",assets.getTotalPages());
        return new ResponseEntity<>(new Response("Danh sách tài sản",data),HttpStatus.OK);
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<Response> getAssetByStatus(@PathVariable Long status,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "assetId") String sort){
        Map<String,Object> data = new HashMap<>();
        Page<Asset> assets = assetService.findAssetByAssetStatus(status,page,size,sort);
        List<AssetResponse> assetResponses = new ArrayList<>();
        for(Asset asset: assets) assetResponses.add(assetMapping.getAssetResponse(asset));
        data.put("assets",assetResponses);
        data.put("totalPage",assets.getTotalPages());
        return new ResponseEntity(new Response("Danh sách tài sản",data),HttpStatus.OK);
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<Response> getAssetByUserId(@PathVariable Long id,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "assetId") String sort){
        Map<String,Object> data = new HashMap<>();
        Page<Asset> assets = assetService.findAssetByUserId(id,page,size,sort);
        List<AssetResponse> assetResponses = new ArrayList<>();
        for(Asset asset: assets) assetResponses.add(assetMapping.getAssetResponse(asset));
        data.put("assets",assetResponses);
        data.put("totalPage",assets.getTotalPages());
        return new ResponseEntity<>(new Response("Danh sách tài sản",data),HttpStatus.OK);
    }
    @PostMapping("/upload-assets-data")
    public ResponseEntity<?> uploadAssetsData(@RequestParam("file") MultipartFile file){
        assetService.saveAssetsToDatabase(file);
        return ResponseEntity
                .ok(Map.of("Message" , " Assets data uploaded and saved to database successfully"));
    }
}
