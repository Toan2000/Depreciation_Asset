package com.example.depreciationService.mapping;

import com.example.depreciationService.client.DepreciationServiceClient;
import com.example.depreciationService.dto.request.DepreciationRequest;
import com.example.depreciationService.dto.response.AssetResponse;
import com.example.depreciationService.dto.response.DepreciationResponse;
import com.example.depreciationService.model.Depreciation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class DepreciationMapping {
    private final DepreciationServiceClient depreciationServiceClient;

    public Depreciation requestToEntity(DepreciationRequest depreciationRequest){
        Depreciation depreciation = new Depreciation();
        depreciation.setActive(true);
        depreciation.setStatus(1);
        depreciation.setDeptId(depreciationRequest.getDeptId());
        depreciation.setAssetId(depreciationRequest.getAssetId());
        depreciation.setUserId(depreciationRequest.getUserId());
        depreciation.setCreateAt(new Date());
        depreciation.setFromDate(new Date());
        return depreciation;
    }

    public DepreciationResponse EntityToResponse(Depreciation depreciation){
        DepreciationResponse depreciationResponse = new DepreciationResponse();
        AssetResponse assetResponse = depreciationServiceClient.fetchAsset(depreciation.getAssetId());
        depreciationResponse.setAssetResponse(assetResponse);
        depreciationResponse.setId(depreciation.getId());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        depreciationResponse.setFromDate(dateFormat.format(depreciation.getFromDate()));
        if(depreciation.getToDate()==null){
            depreciationResponse.setToDate(null);
            depreciationResponse.setAmountMonth(new Date().getMonth() - depreciation.getFromDate().getMonth());
            depreciationResponse.setValueDepreciation(TimeUnit.DAYS.convert(Math.abs(new Date().getTime() - depreciation.getFromDate().getTime()), TimeUnit.MILLISECONDS)*(assetResponse.getPrice()/366));
        }
        else{
            depreciationResponse.setToDate(dateFormat.format(depreciation.getToDate()));
            depreciationResponse.setAmountMonth(depreciation.getToDate().getMonth() - depreciation.getFromDate().getMonth());
            depreciationResponse.setValueDepreciation(TimeUnit.DAYS.convert(Math.abs(depreciation.getToDate().getTime() - depreciation.getFromDate().getTime()), TimeUnit.MILLISECONDS)*(assetResponse.getPrice()/366));
        }
        depreciationResponse.setCreateAt(dateFormat.format(depreciation.getCreateAt()));
        depreciationResponse.setActive(depreciation.isActive());
        depreciationResponse.setUserResponse(depreciationServiceClient.fetchUser(depreciation.getUserId()));
        return depreciationResponse;
    }

    public Depreciation updateDepreciation(Depreciation depreciation){
        Date endDate = new Date();
        depreciation.setToDate(endDate);
        depreciation.setAmountMonth(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        depreciation.setValueDepreciation(depreciationServiceClient.getDepreciationValue(depreciation.getAssetId(),dateFormat.format(depreciation.getFromDate()), dateFormat.format(new Date())));
        depreciation.setStatus(2);
        return depreciation;
    }

}
