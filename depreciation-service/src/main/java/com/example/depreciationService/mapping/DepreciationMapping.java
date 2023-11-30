package com.example.depreciationService.mapping;

import com.example.depreciationService.client.DepreciationServiceClient;
import com.example.depreciationService.dto.request.DepreciationRequest;
import com.example.depreciationService.dto.response.AssetResponse;
import com.example.depreciationService.dto.response.DepreciationByAssetResponse;
import com.example.depreciationService.dto.response.DepreciationResponse;
import com.example.depreciationService.dto.response.UserResponse;
import com.example.depreciationService.model.Depreciation;
import com.example.depreciationService.service.DepreciationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class DepreciationMapping {
    private final DepreciationServiceClient depreciationServiceClient;
    private final DepreciationHistoryService depreciationHistoryService;

    public Depreciation requestToEntity(DepreciationRequest depreciationRequest){
        Depreciation depreciation = new Depreciation();
        depreciation.setActive(true);
        depreciation.setStatus(1);
        depreciation.setDeptId(depreciationRequest.getDeptId());
        depreciation.setAssetId(depreciationRequest.getAssetId());
        AssetResponse assetResponse = depreciationServiceClient.fetchAsset(depreciation.getAssetId());
        depreciation.setAssetTypeId(assetResponse.getAssetTypeId());
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
            depreciationResponse.setValueDepreciation(depreciationServiceClient.getDepreciationValue(depreciation.getAssetId(),dateFormat.format(depreciation.getFromDate()) ,dateFormat.format(new Date())));
        }
        else{
            depreciationResponse.setToDate(dateFormat.format(depreciation.getToDate()));
            depreciationResponse.setAmountMonth(depreciation.getToDate().getMonth() - depreciation.getFromDate().getMonth());
            depreciationResponse.setValueDepreciation(depreciation.getValueDepreciation());
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

    public DepreciationByAssetResponse getDepreciationAssetResponse(Long assetId, List<Depreciation> lDepreciation){
        DepreciationByAssetResponse depreciationByAssetResponse = new DepreciationByAssetResponse();
        Double valuePrev = 0.0;
        Double valuePre = 0.0;
        AssetResponse assetResponse = depreciationServiceClient.fetchAsset(assetId);
        List<DepreciationByAssetResponse.DepreciationAssetHistory> list = new ArrayList<>();
        for(Depreciation depreciation: lDepreciation){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            UserResponse userResponse = depreciationServiceClient.fetchUser(depreciation.getUserId());
            Double value = 0.0;
            Date toDate = depreciation.getToDate();
            if(toDate == null){
                toDate = new Date();
                Object object = depreciationHistoryService.getValueHistoryByDepreciation(toDate.getMonth()+1,toDate.getYear()+1900, depreciation.getId());
                value += object != null ? Double.valueOf(((Object[])object)[1].toString()): 0.0;
                valuePrev += value;
                valuePre = depreciationServiceClient.getDepreciationValue(depreciation.getAssetId(), (toDate.getYear()+1900)+"-"+(toDate.getMonth()+1)+"-01", dateFormat.format(toDate));
                value += valuePre;
            }else valuePrev+=depreciation.getValueDepreciation();
            long amountDate = TimeUnit.DAYS.convert(Math.abs(toDate.getTime() - depreciation.getFromDate().getTime()), TimeUnit.MILLISECONDS);
            list.add(new DepreciationByAssetResponse.DepreciationAssetHistory(depreciation.getId()
                    ,userResponse
                    ,dateFormat.format(depreciation.getFromDate())
                    ,depreciation.getToDate()==null?"Đang sử dụng": dateFormat.format(depreciation.getToDate())
                    ,depreciation.getValueDepreciation()==null?value: depreciation.getValueDepreciation()
                    ,amountDate ));
        }
        depreciationByAssetResponse.setValuePre(valuePre);
        depreciationByAssetResponse.setValuePrev(valuePrev);
        depreciationByAssetResponse.setAmountMonth(assetResponse.getAmountOfYear());
        depreciationByAssetResponse.setAssetId(assetId);
        depreciationByAssetResponse.setAssetName(assetResponse.getAssetName());
        depreciationByAssetResponse.setPrice(assetResponse.getPrice());
        depreciationByAssetResponse.setFromDate(assetResponse.getDateUsed());
        depreciationByAssetResponse.setExpDate(assetResponse.getExpDate());
        depreciationByAssetResponse.setTotalValue(assetResponse.getPrice()-valuePre-valuePrev);
        depreciationByAssetResponse.setChangePrice("Không");
        depreciationByAssetResponse.setListDepreciationAssetHistory(list);
        return depreciationByAssetResponse;
    }

}
