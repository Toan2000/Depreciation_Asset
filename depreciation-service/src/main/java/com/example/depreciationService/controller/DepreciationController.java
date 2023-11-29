package com.example.depreciationService.controller;

import com.example.depreciationService.client.DepreciationServiceClient;
import com.example.depreciationService.dto.request.DepreciationRequest;
import com.example.depreciationService.dto.response.AssetDepreciationResponse;
import com.example.depreciationService.dto.response.DepreciationResponse;
import com.example.depreciationService.mapping.DepreciationHistoryMapping;
import com.example.depreciationService.mapping.DepreciationMapping;
import com.example.depreciationService.model.Depreciation;
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
    private final DepreciationServiceClient depreciationServiceClient;
    private final DepreciationHistoryMapping depreciationHistoryMapping;
    @PostMapping("/create")
    public ResponseEntity saveDepreciation(@RequestBody DepreciationRequest depreciationRequest){
        for(Depreciation depreciation : depreciationService.findByAssetId(depreciationRequest.getAssetId())){
            if(depreciation.getToDate()==null)
                return new ResponseEntity(new String("Thông tin khấu hao đã tồn tại"), HttpStatus.NOT_ACCEPTABLE);
        }
        Depreciation depreciation = depreciationMapping.requestToEntity(depreciationRequest);
        if(depreciationService.saveDepreciation(depreciation))
            return new ResponseEntity(new String("Thông tin khấu hao đã được tạo"), HttpStatus.CREATED);
        return new ResponseEntity(new String("Thông tin khấu hao chưa được tạo"),HttpStatus.NOT_ACCEPTABLE);
    }
    @PutMapping("/recall/{id}")
    public ResponseEntity updateDepreciation(@PathVariable Long id) throws ParseException {
        Depreciation depreciation = depreciationService.findDepreciationToUpdate(id);
        depreciation = depreciationMapping.updateDepreciation(depreciation);
        if(depreciationService.saveDepreciation(depreciation)){
            depreciationHistoryService.saveDepreciationHistory(depreciationHistoryMapping.getHistory(depreciation));
            return new ResponseEntity(new String("Lưu thông tin khấu hao thành công"), HttpStatus.CREATED);
        }
        return new ResponseEntity(new String("Lưu thông tin khấu hao thất bại"),HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("")
    public ResponseEntity getDepreciation(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size){
        Page<Depreciation> listDepreciation = depreciationService.getAllDepreciation(page,size);
        List<DepreciationResponse> depreciationResponses = new ArrayList<>();
        for(Depreciation depreciation: listDepreciation) depreciationResponses.add(depreciationMapping.EntityToResponse(depreciation));
        return new ResponseEntity<>(depreciationResponses, HttpStatus.OK);
    }

    @GetMapping("/perMonth")
    public ResponseEntity getDepreciationPerMonth(@RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        Date sDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        Date eDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        List<Depreciation> depreciationList = depreciationService.getDepreciationByFromDateAndToDate(sDate,eDate);
        Map<String, Object> data = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for(Depreciation depreciation: depreciationList){
            if(depreciation.getToDate()!= null){
                if(depreciation.getFromDate().before(sDate)&&depreciation.getToDate().before(eDate))
                    data.put(depreciation.getId().toString(),depreciationServiceClient.getDepreciationValue(depreciation.getAssetId(), dateFormat.format(sDate), dateFormat.format(depreciation.getToDate())));
                else if(depreciation.getFromDate().after(sDate)&&depreciation.getToDate().before(eDate))
                    data.put(depreciation.getId().toString(),depreciationServiceClient.getDepreciationValue(depreciation.getAssetId(), dateFormat.format(depreciation.getFromDate()),dateFormat.format(depreciation.getToDate())));
                else if(depreciation.getFromDate().after(sDate)&&depreciation.getToDate().after(eDate))
                    data.put(depreciation.getId().toString(),depreciationServiceClient.getDepreciationValue(depreciation.getAssetId(), dateFormat.format(depreciation.getFromDate()),dateFormat.format(depreciation.getToDate())));
            }else {
                if(depreciation.getFromDate().before(sDate))
                    data.put(depreciation.getId().toString(),depreciationServiceClient.getDepreciationValue(depreciation.getAssetId(), dateFormat.format(sDate),dateFormat.format(eDate)));
                else
                    data.put(depreciation.getId().toString(),depreciationServiceClient.getDepreciationValue(depreciation.getAssetId(), dateFormat.format(depreciation.getFromDate()),dateFormat.format(eDate)));
            }
        }
        return new ResponseEntity(data,HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity getDepreciationValue(@RequestParam(defaultValue = "-1") int month,
                                               @RequestParam(defaultValue = "-1") int year){
        return new ResponseEntity(depreciationHistoryMapping.entityToResponse(month,year),HttpStatus.OK);
    }
    @GetMapping("/history/{id}")
    public ResponseEntity getDepreciationValue(@PathVariable Long id){
        return new ResponseEntity(depreciationHistoryMapping.getEntityToResponse(id),HttpStatus.OK);
    }

    @GetMapping("/dept/history")
    public ResponseEntity getDepreciationValueAllDept(@RequestParam(defaultValue = "-1") int month,
                                                      @RequestParam(defaultValue = "-1") int year){
        return new ResponseEntity(depreciationHistoryMapping.getDepreciationDeptResponse(depreciationHistoryService.getDepreciationByAllDept(month, year)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getDepreciationByAssetId(@PathVariable Long assetId){
        return null;
    }


}
