package com.example.depreciationService.mapping;

import com.example.depreciationService.client.DepreciationServiceClient;
import com.example.depreciationService.dto.response.AssetDepreciationResponse;
import com.example.depreciationService.model.Depreciation;
import com.example.depreciationService.model.DepreciationHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DepreciationHistoryMapping {
    private final DepreciationServiceClient depreciationServiceClient;

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

    public Map<Long,AssetDepreciationResponse> entityToResponse(List<Object> valueDepreciation){
        Map<Long,AssetDepreciationResponse> assetDepreciationResponseMap = new HashMap<>();

        for(Object a: valueDepreciation){
            AssetDepreciationResponse assetDepreciationResponse = new AssetDepreciationResponse();
            assetDepreciationResponseMap.put(Long.valueOf(1),assetDepreciationResponse);
        }


        return assetDepreciationResponseMap;

    }

}
