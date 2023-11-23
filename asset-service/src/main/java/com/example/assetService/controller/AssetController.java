package com.example.assetService.controller;

import com.example.assetService.client.AssetServiceClient;
import com.example.assetService.dto.request.AssetRequest;
import com.example.assetService.dto.request.DepreciationRequest;
import com.example.assetService.dto.response.AssetResponse;
import com.example.assetService.dto.response.Response;
import com.example.assetService.mapping.AssetMapping;
import com.example.assetService.model.Asset;
import com.example.assetService.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/asset")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3001")

public class AssetController {
    private final AssetService assetService;
    private final AssetMapping assetMapping;
    private final AssetServiceClient assetServiceClient;
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
    @GetMapping("/v1/{id}")
    public ResponseEntity getAssetByIdv1(@PathVariable Long id){
        return new ResponseEntity(assetMapping.getAssetResponse(assetService.findAssetById(id)),HttpStatus.OK);
    }


//    API create Depreciation
//    @PostMapping("/v1")
//    public ResponseEntity postDepreciation(@RequestBody DepreciationRequest depreciationRequest){
//        assetServiceClient.addDepreciation(depreciationRequest);
//        return new ResponseEntity(HttpStatus.OK);
//    }

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
    @GetMapping("/assetType/{id}")
    public ResponseEntity<Response> getAssetByAssetType(@PathVariable Long id,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        @RequestParam(defaultValue = "assetId") String sort){
        Map<String,Object> data = new HashMap<>();
        Page<Asset> assets = assetService.findAssetByAssetType(id,page,size,sort);
        List<AssetResponse> assetResponses = new ArrayList<>();
        for(Asset asset: assets) assetResponses.add(assetMapping.getAssetResponse(asset));
        data.put("assets",assetResponses);
        data.put("totalPage",assets.getTotalPages());
        return new ResponseEntity<>(new Response("Danh sách tài sản",data),HttpStatus.OK);
    }
    @GetMapping("/date")
    public ResponseEntity<Response> getAssetByDate(@RequestParam(defaultValue = "1900-01-01") String fromDate,
                                                   @RequestParam(defaultValue = "2999-12-31") String toDate,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "assetId") String sort) throws ParseException {
        Map<String,Object> data = new HashMap<>();
        Page<Asset> assets = assetService.findAssetByDate(new SimpleDateFormat("yyyy-MM-dd").parse(fromDate),new SimpleDateFormat("yyyy-MM-dd").parse(toDate),page,size,sort);
        List<AssetResponse> assetResponses = new ArrayList<>();
        for(Asset asset: assets) assetResponses.add(assetMapping.getAssetResponse(asset));
        data.put("assets",assetResponses);
        data.put("totalPage",assets.getTotalPages());
        return new ResponseEntity<>(new Response("Danh sách tài sản",data),HttpStatus.OK);
    }
    @PostMapping("/keyword")
    public ResponseEntity<Response> getAssetByDate(@RequestBody String name,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "asset_id") String sort){
        Map<String,Object> data = new HashMap<>();
        Page<Asset> assets = assetService.findAssetByName(name,page,size,sort);
        List<AssetResponse> assetResponses = new ArrayList<>();
        for(Asset asset: assets) assetResponses.add(assetMapping.getAssetResponse(asset));
        data.put("assets",assetResponses);
        data.put("totalPage",assets.getTotalPages());
        return new ResponseEntity<>(new Response("Danh sách tài sản",data),HttpStatus.OK);
    }
    @GetMapping("/filter")
    public ResponseEntity<Response> filterAssets(@RequestParam(defaultValue = "NAMENULL") String name,
                                                 @RequestParam(defaultValue = "-1") Long dept,
                                                 @RequestParam(defaultValue = "-1") Long user,
                                                 @RequestParam(defaultValue = "-1") Long status,
                                                 @RequestParam(defaultValue = "-1") Long assetType,
                                                 @RequestParam(defaultValue = "1000-01-01") String fromDate,
                                                 @RequestParam(defaultValue = "2999-12-31") String toDate,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "asset_id") String sort) throws ParseException {
        Map<String,Object> data = new HashMap<>();
        Page<Asset> assets = assetService.filterAssets(name,dept,user,status,assetType,
                    new SimpleDateFormat("yyyy-MM-dd").parse(fromDate),
                    new SimpleDateFormat("yyyy-MM-dd").parse(toDate),
                    page,size,sort);
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
    @PostMapping("/create")
    public ResponseEntity<Response> createAsset(@RequestBody AssetRequest assetRequest){
        if(assetService.createAsset(assetMapping.getAsset(assetRequest)))
            return new ResponseEntity(new Response("Tạo tài sản thành công",null),HttpStatus.CREATED);
        return new ResponseEntity(new Response("Tạo tài sản thất bại",null),HttpStatus.NOT_ACCEPTABLE);
    }
    @PutMapping("/user/{id}")
    public ResponseEntity<Response> addUserUsed(@PathVariable Long id,@RequestParam Long userId){
        Asset asset = assetService.findAssetById(id);
        if(asset == null) return new ResponseEntity<>(new Response("Không tìm thấy tài sản",null),HttpStatus.NOT_FOUND);
        asset = assetMapping.updateAsset(asset,userId);
        if(asset == null) return new ResponseEntity<>(new Response("Không tìm thấy người dùng",null),HttpStatus.NOT_FOUND);
        assetService.createAsset(asset);

        return new ResponseEntity<>(new Response("Cập nhật thông tin thành công",null),HttpStatus.OK);
    }
}
