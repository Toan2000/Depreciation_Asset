package com.example.depreciationService.tasks;

import com.example.depreciationService.client.DepreciationServiceClient;
import com.example.depreciationService.model.Depreciation;
import com.example.depreciationService.model.DepreciationHistory;
import com.example.depreciationService.service.DepreciationHistoryService;
import com.example.depreciationService.service.DepreciationService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
@AllArgsConstructor
@Lazy(false)
public class DepreciationHistoryTask {
    private final DepreciationService depreciationService;
    private final DepreciationHistoryService depreciationHistoryService;
    private final DepreciationServiceClient depreciationServiceClient;
    @Scheduled(cron = "00 00 00 L * ?")
    public void calculateDepreciationPerMonth() throws ParseException {
        LocalDate today =LocalDate.now();
        //Khởi tạo ngày đầu tháng và cuối tháng
        Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(today.getYear()+"-"+today.getMonthValue()+"-01");
        Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(today.getYear()+"-"+today.getMonthValue()+"-"+today.lengthOfMonth());
        //Tìm danh sách các khấu hao còn đang chạy và các khấu hao được tạo trong tháng
        List<Depreciation> depreciationList = depreciationService.getDepreciationByFromDateAndToDate(fromDate, toDate);
        for(Depreciation depreciation: depreciationList){
            DepreciationHistory depreciationHistory = new DepreciationHistory();
            depreciationHistory.setCreateAt(new Date());
            depreciationHistory.setMonth(today.getMonthValue());
            depreciationHistory.setYear(today.getYear());
            depreciationHistory.setDepreciation(depreciation);
            depreciationHistory.setAssetId(depreciation.getAssetId());
            depreciationHistory.setAssetId(depreciation.getAssetTypeId());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if(depreciation.getFromDate().before(fromDate))
                depreciationHistory.setValue(depreciationServiceClient.getDepreciationValue(depreciation.getAssetId(), dateFormat.format(fromDate),dateFormat.format(toDate)));
            else
                depreciationHistory.setValue(depreciationServiceClient.getDepreciationValue(depreciation.getAssetId(), dateFormat.format(depreciation.getFromDate()),dateFormat.format(toDate)));
            depreciationHistoryService.saveDepreciationHistory(depreciationHistory);
        }
    }
}
