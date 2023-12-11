package com.example.depreciationService.controller;

import com.example.depreciationService.dto.request.DepreciationByDeptRequest;
import com.example.depreciationService.mapping.DepreciationHistoryMapping;
import com.example.depreciationService.service.DepreciationHistoryService;
import com.example.depreciationService.tasks.DepreciationHistoryTask;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/depreciation/history")
@RequiredArgsConstructor
public class DepreciationHistoryController {
    private final DepreciationHistoryService depreciationHistoryService;
    private final DepreciationHistoryMapping depreciationHistoryMapping;
    private final DepreciationHistoryTask depreciationHistoryTask;
    //Tất cả thông tin khấu hao của phòng ban
    @GetMapping("/dept")
    public ResponseEntity getDepreciationValueAllDept(@RequestParam int year, @RequestParam(required = false) List<Long> ids){
        List<Object> records = new ArrayList<>();
        if(ids.size()==1&&ids.get(0)==0)
            records = depreciationHistoryService.getDepreciationByAllDept(year);
        else
            records = depreciationHistoryService.getDepreciationByDeptIds(year,ids);
        return new ResponseEntity(depreciationHistoryMapping.getDepreciationDeptResponse(records), HttpStatus.OK);
    }
    @GetMapping("/test")
    public ResponseEntity getTest(@RequestParam String text) throws ParseException {
        depreciationHistoryTask.calculateDepreciationPerMonthTest(text);
        return new ResponseEntity("Tính khấu hao thánh công",HttpStatus.OK);
    }
}
