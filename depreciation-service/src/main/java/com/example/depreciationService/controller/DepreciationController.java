package com.example.depreciationService.controller;

import com.example.depreciationService.dto.request.DepreciationRequest;
import com.example.depreciationService.dto.response.DepreciationResponse;
import com.example.depreciationService.mapping.DepreciationMapping;
import com.example.depreciationService.model.Depreciation;
import com.example.depreciationService.service.DepreciationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/depreciation")
@RequiredArgsConstructor
public class DepreciationController {
    private final DepreciationMapping depreciationMapping;
    private final DepreciationService depreciationService;
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
    public ResponseEntity updateDepreciation(@PathVariable Long id){
        Depreciation depreciation = depreciationService.findDepreciationToUpdate(id);
        depreciation = depreciationMapping.updateDepreciation(depreciation);
        if(depreciationService.saveDepreciation(depreciation))
            return new ResponseEntity(new String("Lưu thông tin khấu hao thành công"), HttpStatus.CREATED);
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

}
