package com.example.depreciationService.controller;

import com.example.depreciationService.client.DepreciationServiceClient;
import com.example.depreciationService.dto.request.DepreciationByDeptRequest;
import com.example.depreciationService.dto.request.DepreciationRequest;
import com.example.depreciationService.dto.response.*;
import com.example.depreciationService.mapping.DepreciationHistoryMapping;
import com.example.depreciationService.mapping.DepreciationMapping;
import com.example.depreciationService.model.Depreciation;
import com.example.depreciationService.model.DepreciationHistory;
import com.example.depreciationService.service.DepreciationHistoryService;
import com.example.depreciationService.service.DepreciationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/depreciation")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")

public class DepreciationController {
    private final DepreciationMapping depreciationMapping;
    private final DepreciationService depreciationService;
    private final DepreciationHistoryService depreciationHistoryService;
    private final DepreciationHistoryMapping depreciationHistoryMapping;
    //Tạo thông tin khấu hao
    @PostMapping("/create")
    public ResponseEntity saveDepreciation(@RequestBody DepreciationRequest depreciationRequest) throws ParseException {
        Depreciation depreciation = depreciationService.findByAssetIdAndToDate(depreciationRequest.getAssetId(), null);
        if(depreciation!=null)
            return new ResponseEntity(new String("Thông tin khấu hao đã tồn tại"), HttpStatus.NOT_ACCEPTABLE);
        Object object = depreciationService.findLDateAndSumValueByAssetId(depreciationRequest.getAssetId());
        Depreciation depreciationRecords = depreciationMapping.requestToEntity(depreciationRequest, object);
        if(depreciationService.saveDepreciation(depreciationRecords))
            return new ResponseEntity(new String("Thông tin khấu hao đã được tạo"), HttpStatus.CREATED);
        return new ResponseEntity(new String("Thông tin khấu hao chưa được tạo"),HttpStatus.NOT_ACCEPTABLE);
    }

    //API Thực hiện tính toán và ngưng khấu hao
    @GetMapping("/recall/{id}")
    public ResponseEntity updateDepreciation(@PathVariable Long id) throws ParseException {
        Depreciation depreciation = depreciationService.findDepreciationToUpdate(id);
        depreciation = depreciationMapping.updateDepreciation(depreciation);
        if(depreciationService.saveDepreciation(depreciation)){
            return new ResponseEntity(true, HttpStatus.CREATED);
        }
        return new ResponseEntity(false,HttpStatus.NOT_ACCEPTABLE);
    }
    //Đếm các giá trị khấu hao đến hiện tại
    @GetMapping("/count")
    public ResponseEntity countDepreciationValue(){
        return new ResponseEntity(depreciationHistoryService.totalValueDepreciation(),HttpStatus.OK);
    }
    //Thông tin khấu hao theo mỗi tài sản
    @GetMapping("/asset/{id}")
    public ResponseEntity getDepreciationByAssetId(@PathVariable Long id) throws ParseException {
        List<Depreciation> depreciationList =  depreciationService.findByAssetIdOrderByIdAsc(id);
        DepreciationByAssetResponse depreciation = depreciationMapping.getDepreciationAssetResponse(id,depreciationList);
        return new ResponseEntity(depreciation,HttpStatus.OK);
    }
//    @GetMapping("/test12")
//    public ResponseEntity countDe(){
//        Double result = depreciationHistoryService.getTotalValueByDeptIdAndAssetType(Long.valueOf(2),Long.valueOf(2),2023);
//        if(result == null)
//            result = 0.0;
//        return new ResponseEntity(result,HttpStatus.OK);
//    }
//    @GetMapping("/history")
//    public ResponseEntity getDepreciationValue(@RequestParam(defaultValue = "-1") int month,
//                                               @RequestParam(defaultValue = "-1") int year){
//        return new ResponseEntity(depreciationHistoryMapping.entityToResponse(month,year),HttpStatus.OK);
//    }
    //Tìm thông tin records Depreciation trong DB
//    @GetMapping("")
//    public ResponseEntity getDepreciation(@RequestParam(defaultValue = "0") int page,
//                                          @RequestParam(defaultValue = "10") int size){
//        Page<Depreciation> listDepreciation = depreciationService.getAllDepreciation(page,size);
//        List<DepreciationResponse> depreciationResponses = new ArrayList<>();
//        for(Depreciation depreciation: listDepreciation) depreciationResponses.add(depreciationMapping.EntityToResponse(depreciation));
//        return new ResponseEntity<>(depreciationResponses, HttpStatus.OK);
//    }

//    @GetMapping("/perMonth")
//    public ResponseEntity getDepreciationPerMonth(@RequestParam String startDate, @RequestParam String endDate) throws ParseException {
//        Date sDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
//        Date eDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
//        List<Depreciation> depreciationList = depreciationService.getDepreciationByFromDateAndToDate(sDate,eDate);
//        Map<String, Object> data = new HashMap<>();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        for(Depreciation depreciation: depreciationList){
//            if(depreciation.getToDate()!= null){
//                if(depreciation.getFromDate().before(sDate)&&depreciation.getToDate().before(eDate))
//                    data.put(depreciation.getId().toString(),depreciationServiceClient.getDepreciationValue(depreciation.getAssetId(), dateFormat.format(sDate), dateFormat.format(depreciation.getToDate())));
//                else if(depreciation.getFromDate().after(sDate)&&depreciation.getToDate().before(eDate))
//                    data.put(depreciation.getId().toString(),depreciationServiceClient.getDepreciationValue(depreciation.getAssetId(), dateFormat.format(depreciation.getFromDate()),dateFormat.format(depreciation.getToDate())));
//                else if(depreciation.getFromDate().after(sDate)&&depreciation.getToDate().after(eDate))
//                    data.put(depreciation.getId().toString(),depreciationServiceClient.getDepreciationValue(depreciation.getAssetId(), dateFormat.format(depreciation.getFromDate()),dateFormat.format(depreciation.getToDate())));
//            }else {
//                if(depreciation.getFromDate().before(sDate))
//                    data.put(depreciation.getId().toString(),depreciationServiceClient.getDepreciationValue(depreciation.getAssetId(), dateFormat.format(sDate),dateFormat.format(eDate)));
//                else
//                    data.put(depreciation.getId().toString(),depreciationServiceClient.getDepreciationValue(depreciation.getAssetId(), dateFormat.format(depreciation.getFromDate()),dateFormat.format(eDate)));
//            }
//        }
//        return new ResponseEntity(data,HttpStatus.OK);
//    }

//    @GetMapping("/history/{id}")
//    public ResponseEntity getDepreciationValue(@PathVariable Long id){
//        return new ResponseEntity(depreciationHistoryMapping.getEntityToResponse(id),HttpStatus.OK);
//    }

//    @GetMapping("/test")
//    public ResponseEntity test(){
//        List<AssetType> assetTypes = new ArrayList<>();
//        Map<String,Double> data = new HashMap<>();
//        for(Object o: depreciationHistoryService.getDepreciationByAllDeptInYear(2023,Long.valueOf(2))){
//            Long idType = Long.valueOf(((Object[])o)[1].toString());
//            Long month = Long.valueOf(((Object[])o)[2].toString());
//            Double value = Double.valueOf(((Object[])o)[3].toString());
//            System.out.println(idType+" -"+month+ " "+value);
//            AssetType assetType = assetTypes.stream()
//                    .filter(type -> type.getTypeId() == idType)
//                    .findFirst()
//                    .orElse(null);
//            if(assetType == null){
//                assetType = new AssetType();
//                AssetTypeResponse assetTypeResponse = depreciationServiceClient.fetchAssetType(idType);
//                assetType.setTypeId(idType);
//                assetType.setTypeName(assetTypeResponse.getAssetName());
//                assetType.setPrice(value);
//            }else{
//                if(data.get(month.toString())==null)
//                    data.put(month.toString(),Double.valueOf(((Object[])o)[3].toString()));
//                else{
//                    data.put(month.toString(),value+Double.valueOf(data.get(((Object[])o)[2].toString())));
//                }
//            }
//        }
//        return new ResponseEntity(depreciationHistoryService.getDepreciationByAllDeptInYear(2023,Long.valueOf(2)),HttpStatus.OK);
//    }
    //Thông tin khấu hao từng tháng theo mã khấu hao
//    @GetMapping("/history/{id}")
//    public ResponseEntity getHistoryDepreciationByDepreciationId(@PathVariable Long id){
//        Depreciation depreciation = depreciationService.findById(id);
//        return new ResponseEntity(depreciationHistoryMapping.getDepreciationHistoryByDepreciation(depreciation),HttpStatus.OK);
//    }

}
