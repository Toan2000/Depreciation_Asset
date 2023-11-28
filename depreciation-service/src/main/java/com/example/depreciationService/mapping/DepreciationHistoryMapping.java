package com.example.depreciationService.mapping;

import com.example.depreciationService.client.DepreciationServiceClient;
import com.example.depreciationService.dto.response.AssetDepreciationResponse;
import com.example.depreciationService.dto.response.AssetResponse;
import com.example.depreciationService.model.Depreciation;
import com.example.depreciationService.model.DepreciationHistory;
import com.example.depreciationService.service.DepreciationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DepreciationHistoryMapping {
    private final DepreciationServiceClient depreciationServiceClient;
    private final DepreciationHistoryService depreciationHistoryService;

    public DepreciationHistory getHistory(Depreciation depreciation) throws ParseException {
        Date fromDate = new Date();
        fromDate.setDate(1);
        DepreciationHistory depreciationHistory = new DepreciationHistory();
        depreciationHistory.setCreateAt(new Date());
        depreciationHistory.setMonth(fromDate.getMonth()+1);
        depreciationHistory.setYear(fromDate.getYear()+1900);
        depreciationHistory.setDepreciation(depreciation);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(depreciation.getFromDate().before(fromDate))
            depreciationHistory.setValue(depreciationServiceClient.getDepreciationValue(depreciation.getAssetId(), dateFormat.format(fromDate),dateFormat.format(new Date())));
        else
            depreciationHistory.setValue(depreciationServiceClient.getDepreciationValue(depreciation.getAssetId(), dateFormat.format(depreciation.getFromDate()),dateFormat.format(new Date())));
        return depreciationHistory;
    }

    public List<AssetDepreciationResponse> entityToResponse(int month, int year){
        List<AssetDepreciationResponse> assetDepreciationResponseList = new ArrayList<>();
        for(Object a: depreciationHistoryService.getDepreciationValue(month, year)){
            Long assetId = Long.valueOf(((Object[])a)[0].toString());
            AssetResponse assetResponse = depreciationServiceClient.fetchAsset(assetId);
            AssetDepreciationResponse assetDepreciationResponse = new AssetDepreciationResponse();
            assetDepreciationResponse.setAssetId(assetId);
            assetDepreciationResponse.setSerialNumber(assetResponse.getSerial());
            assetDepreciationResponse.setPrice(assetResponse.getPrice());
            assetDepreciationResponse.setFromDate(assetResponse.getDateUsed());
            assetDepreciationResponse.setValuePerMonth(assetResponse.getPrice()/assetResponse.getAmountOfYear());
            assetDepreciationResponse.setAmountMonth(assetResponse.getAmountOfYear());
            Double accumulated = Double.valueOf(((Object[])a)[1].toString());
            assetDepreciationResponse.setAccumulated(accumulated);
            assetDepreciationResponse.setValuePresent(assetResponse.getPrice()-accumulated);
            assetDepreciationResponse.setAmountDayOfMonth(LocalDate.of(year,month,1).lengthOfMonth());
            assetDepreciationResponse.setAmountDateDepreciation(LocalDate.of(year,month,1).lengthOfMonth());
            LocalDate localDate = LocalDate.of(year,month,1).minusDays(1);
            Object accumulatedPrev = depreciationHistoryService.getValueByMonthAndYearAndAsset(localDate.getMonthValue(), localDate.getYear(), assetId);
            assetDepreciationResponse.setAccumulatedPrev(Double.valueOf(accumulatedPrev != null ? ((Object[])accumulatedPrev)[1].toString() : "0"));
            Object accumulatedPresent = depreciationHistoryService.getValueByMonthAndYearAndAsset(month, year,assetId);
            assetDepreciationResponse.setAccumulatedPresent(Double.valueOf((accumulatedPresent != null ? ((Object[])accumulatedPresent)[1].toString() :"0")));
            Map<String,Object> months = new HashMap<>();
            for(Object b: depreciationHistoryService.getValueByYear(year,assetId))
                months.put(((Object[])b)[0].toString(),Double.valueOf(((Object[])b)[1].toString()));
            assetDepreciationResponse.setMonths(months);
            Object o = depreciationHistoryService.getValueByMonthAndYearAndAsset(1, year,assetId);
            Double valueYearPrev = 0.0;
            if(o != null)
                valueYearPrev = Double.valueOf(((Object[])o)[1].toString());
            assetDepreciationResponse.setAccumulatedPresentPrev(assetResponse.getPrice()-valueYearPrev);
            assetDepreciationResponse.setAccumulatedYearPrev(valueYearPrev);
            assetDepreciationResponse.setAssetName(assetResponse.getAssetName());
            assetDepreciationResponseList.add(assetDepreciationResponse);
        }
        return assetDepreciationResponseList;

    }

    public AssetDepreciationResponse getEntityToResponse(Long assetId){
        Date date = new Date();
        AssetDepreciationResponse assetDepreciationResponse = new AssetDepreciationResponse();
        AssetResponse assetResponse = depreciationServiceClient.fetchAsset(assetId);
        assetDepreciationResponse.setAssetId(assetId);
        assetDepreciationResponse.setSerialNumber(assetResponse.getSerial());
        assetDepreciationResponse.setPrice(assetResponse.getPrice());
        assetDepreciationResponse.setFromDate(assetResponse.getDateUsed());
        assetDepreciationResponse.setValuePerMonth(assetResponse.getPrice()/assetResponse.getAmountOfYear());
        assetDepreciationResponse.setAmountMonth(assetResponse.getAmountOfYear());
        // Giá trị khấu hao kì này
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Double depreciationPresent = depreciationServiceClient.getDepreciationValue(assetId,(date.getYear()+1900)+"-"+(date.getMonth()+1)+"-01",dateFormat.format(date));
        // Giá trị đã khấu hao
        Object accumulated = depreciationHistoryService.getValueHistoryBy(date.getMonth()+1, date.getYear()+1900, assetId);
        if(accumulated!= null){
            assetDepreciationResponse.setAccumulated(Double.valueOf(((Object[])accumulated)[1].toString()));
            assetDepreciationResponse.setValuePresent(assetResponse.getPrice()-Double.valueOf(((Object[])accumulated)[1].toString()));
        }
        else {
            assetDepreciationResponse.setAccumulated(0.0);
            assetDepreciationResponse.setValuePresent(assetResponse.getPrice());
        }
        assetDepreciationResponse.setAmountDayOfMonth(LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse((date.getYear()+1900)+"-"+(date.getMonth()+1)+"-01")).lengthOfMonth());
        assetDepreciationResponse.setAmountDateDepreciation(date.getDate());
        Map<String,Object> months = new HashMap<>();
        assetDepreciationResponse.setAssetName(assetResponse.getAssetName());
        return assetDepreciationResponse;
    }

}
