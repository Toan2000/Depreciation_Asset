package com.example.assetService.controller;

import com.example.assetService.dto.AssetResponse;
import com.example.assetService.dto.Response;
import com.example.assetService.model.Asset;
import com.example.assetService.service.AssetService;
import lombok.RequiredArgsConstructor;
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
public class AssetController {
    private final AssetService assetService;
    @GetMapping("")
    public ResponseEntity<Response> getAllAsset(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "assetId") String sort){
        Map<String,Object> data = new HashMap<>();
        List<AssetResponse> assetResponses = new ArrayList<>();
        for(Asset asset: assetService.getAssets(page,size,sort))
            assetResponses.add(assetService.getAssetResponse(asset));
        data.put("assets",assetResponses);
        return new ResponseEntity<>(new Response("Danh sách tài sản",data),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Response> getAssetById(@PathVariable Long id){
        Map<String,Object> data = new HashMap<>();
        data.put("asset",assetService.findAssetById(id));
        return new ResponseEntity<>(new Response("Thông tin tài sản",data),HttpStatus.OK);
    }
    @GetMapping("/dept/{id}")
    public ResponseEntity<Response> getAssetByDeptId(@PathVariable Long id,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "assetId") String sort){
        Map<String,Object> data = new HashMap<>();
        data.put("assets",assetService.findAssetByDeptId(id,page,size,sort));
        return new ResponseEntity<>(new Response("Danh sách tài sản",data),HttpStatus.OK);
    }
    @GetMapping("/status/{status}")
    public ResponseEntity getAssetByStatus(@PathVariable Long status,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "assetId") String sort){
        return new ResponseEntity(assetService.findAssetByAssetStatus(status,page,size,sort),HttpStatus.OK);
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<Response> getAssetByUserId(@PathVariable Long id,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "assetId") String sort){
        Map<String,Object> data = new HashMap<>();
        data.put("assets",assetService.findAssetByUserId(id,page,size,sort));
        return new ResponseEntity<>(new Response("Danh sách tài sản",data),HttpStatus.OK);
    }
    @PostMapping("/upload-assets-data")
    public ResponseEntity<?> uploadAssetsData(@RequestParam("file") MultipartFile file){
        assetService.saveAssetsToDatabase(file);
        return ResponseEntity
                .ok(Map.of("Message" , " Assets data uploaded and saved to database successfully"));
    }
}
