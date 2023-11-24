package com.example.depreciationService.client;

import com.example.depreciationService.dto.response.AssetResponse;
import com.example.depreciationService.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Component
@NoArgsConstructor
public class DepreciationServiceClient {
    @Autowired
    private RestTemplate template;

    public AssetResponse fetchAsset(Long assetId) {
        return template.getForObject("http://ASSET-SERVICE/api/asset/v1/" + assetId, AssetResponse.class);
    }
    public UserResponse fetchUser(Long userId) {
        return template.getForObject("http://USER-SERVICE/api/user/v1/" + userId, UserResponse.class);
    }

    public Double getDepreciationValue(Long assetId, String fromDate, String toDate){
        return template.getForObject("http://ASSET-SERVICE/api/asset/depreciation/" + assetId +"?fromDate="+fromDate+"&toDate="+toDate, Double.class);
    }
}
