package com.example.assetService.controller;

import com.example.assetService.client.AssetServiceClient;
import com.example.assetService.dto.request.AssetRequest;
import com.example.assetService.dto.request.DeliveryRequest;
import com.example.assetService.dto.request.DepreciationRequest;
import com.example.assetService.dto.request.UpdateHistoryRequest;
import com.example.assetService.dto.response.AssetResponse;
import com.example.assetService.dto.response.Response;
import com.example.assetService.mapping.AssetMapping;
import com.example.assetService.model.Asset;
import com.example.assetService.model.AssetType;
import com.example.assetService.model.UpdateHistory;
import com.example.assetService.service.AssetService;
import com.example.assetService.service.AssetTypeService;
import com.example.assetService.service.UpdateHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/asset")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AssetController {
    private final AssetService assetService;
    private final AssetMapping assetMapping;
    private final AssetTypeService assetTypeService;
    private final AssetServiceClient assetServiceClient;
    private final UpdateHistoryService updateHistoryService;
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
    @GetMapping("/type")
    public ResponseEntity getAllAssetType(){
        return new ResponseEntity(assetTypeService.getAllAsset(), HttpStatus.OK);
    }
    @GetMapping("/type/{id}")
    public ResponseEntity getAllAssetTypeById(@PathVariable Long id){
        return new ResponseEntity(assetTypeService.findAssetTypeById(id), HttpStatus.OK);
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
    //Bộ lọc tài sản
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
    //Thực hiện tạo thông tin tài sản thông qua file Excel
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
    public ResponseEntity<Response> addUserUsed(@PathVariable Long id, @RequestBody DeliveryRequest deliveryRequest){
        Asset asset = assetService.findAssetById(id);
        if(asset == null) return new ResponseEntity<>(new Response("Không tìm thấy tài sản",null),HttpStatus.NOT_FOUND);
        if(asset.getUserUsedId() != null) return new ResponseEntity<>(new Response("Tài sản đang được sử dụng",null),HttpStatus.NOT_FOUND);
        asset = assetMapping.deliveryAsset(asset,deliveryRequest);
        if(asset == null) return new ResponseEntity<>(new Response("Bàn giao tài sản thất bại",null),HttpStatus.NOT_FOUND);
        assetService.createAsset(asset);
        return new ResponseEntity<>(new Response("Cập nhật thông tin thành công",null),HttpStatus.OK);
    }
    @PutMapping("/recall/{id}")
    public ResponseEntity<Response> recallAsset(@PathVariable Long id,@RequestParam(required = false) String note){
        Asset asset = assetService.findAssetById(id);
        if(asset == null) return new ResponseEntity<>(new Response("Không tìm thấy tài sản",null),HttpStatus.NOT_FOUND);
        if(asset.getUserUsedId() == null) return new ResponseEntity<>(new Response("Tài sản chưa được đưa vào sử dụng",null),HttpStatus.NOT_FOUND);
        asset = assetMapping.recallAsset(asset,note);
        if(asset == null) return new ResponseEntity<>(new Response("Thu hồi tài sản thất bại",null),HttpStatus.NOT_FOUND);
        assetService.createAsset(asset);
        return new ResponseEntity<>(new Response("Cập nhật thông tin thành công",null),HttpStatus.OK);
    }

    @GetMapping("/depreciation/{id}")
    public ResponseEntity getDepreciationPerMonth(@PathVariable Long id,
                                                  @RequestParam String fromDate,
                                                  @RequestParam String toDate) throws ParseException {
        Asset asset = assetService.findAssetById(id);
        LocalDate fDate = LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate tDate = LocalDate.parse(toDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Double valuePerMonth = asset.getPrice()/asset.getTime();
        Double value = 0.0;
        if(fDate.getMonthValue()==tDate.getMonthValue()&&tDate.getYear()==fDate.getYear())
            value += valuePerMonth*((tDate.getDayOfMonth()-fDate.getDayOfMonth()+1)/Double.valueOf(fDate.lengthOfMonth()));
        else {
            value += valuePerMonth*((fDate.lengthOfMonth()-fDate.getDayOfMonth()+1)/Double.valueOf(fDate.lengthOfMonth()));
            for(int i = fDate.getYear(); i<= tDate.getYear();i++){
                for(int j =fDate.getMonthValue()+1;j<=12;j++){
                    if(fDate.getYear() == tDate.getYear()&& j == tDate.getMonthValue())
                        break;
                    else
                        value += valuePerMonth;
                }
            }
            value += valuePerMonth*(tDate.getDayOfMonth()/Double.valueOf(tDate.lengthOfMonth()));
        }
        return new ResponseEntity(value,HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity updateAsset(@PathVariable Long id,@RequestBody UpdateHistoryRequest updateHistoryRequest) throws ParseException {
        Asset asset = assetService.findAssetById(id);
        if(asset == null)
            return new ResponseEntity("Tài sản không tồn tại",HttpStatus.NOT_FOUND);
        if(asset.getUserUsedId() != null|| asset.getDeptUsedId() != null)
            return new ResponseEntity("Tài sản vẫn còn đang sử dụng", HttpStatus.NOT_ACCEPTABLE);
        asset = assetMapping.updateAsset(asset,updateHistoryRequest);
        if(assetService.createAsset(asset)){
            return new ResponseEntity("Nâng cấp tài sản thành công",HttpStatus.OK);
        }
        return new ResponseEntity("Nâng cấp tài sản thất bại",HttpStatus.OK);
    }


    //Hàm tính khấu hao theo hai ngày trong 1 tháng
    @GetMapping("/depreciation/test/{id}")
    public ResponseEntity getDepreciationPerDate(@PathVariable Long id,
                                                 @RequestParam String fromDate,
                                                 @RequestParam String toDate,
                                                 @RequestParam Double value,
                                                 @RequestParam String lastDate) throws ParseException {
        return new ResponseEntity(assetMapping.calculatorDepreciation(assetService.findAssetById(id),fromDate,toDate,value,lastDate),HttpStatus.OK);
    }
    //Hàm tính khấu hao theo tháng
    @GetMapping("/depreciation/month/{id}")
    public ResponseEntity getDepreciationPerMonth(@PathVariable Long id,
                                                  @RequestParam Double value,
                                                  @RequestParam String lastDate) throws ParseException {
        return new ResponseEntity(assetMapping.calculatorDepreciationPerMonth(assetService.findAssetById(id),value,lastDate),HttpStatus.OK);
    }
    @GetMapping("/count")
    public ResponseEntity getCountAsset(){
        return new ResponseEntity(assetService.countAsset(),HttpStatus.OK);
    }

    @GetMapping("/delivery/{id}")
    public ResponseEntity getAssetDelivery(@PathVariable Long id){
        return new ResponseEntity(assetMapping.getAssetDeliveryResponse(assetService.findAssetById(id)),HttpStatus.OK);
    }
    @GetMapping("/update/{id}")
    public ResponseEntity getAllUpdateHistory(@PathVariable Long id) throws ParseException {
        Asset asset = assetService.findAssetById(id);
        List<UpdateHistory> histories = updateHistoryService.getListUpdateHistoryByAssetId(id);
        return new ResponseEntity(assetMapping.getAssetUpdateHistoryResponse(asset,histories),HttpStatus.OK);
    }
    @GetMapping("/reduce/{id}")
    public ResponseEntity getAllReduceHistory(@PathVariable Long id) throws ParseException {
        Asset asset = assetService.findAssetById(id);
        List<UpdateHistory> histories = updateHistoryService.getListReduceHistoryByAssetId(id);
        return new ResponseEntity(assetMapping.getAssetUpdateHistoryResponse(asset,histories),HttpStatus.OK);
    }
}
