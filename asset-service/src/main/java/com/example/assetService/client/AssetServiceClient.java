package com.example.assetService.client;

import com.example.assetService.dto.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AssetServiceClient {

    @Autowired
    private RestTemplate template;

    public UserResponse fetchUser(Long userId) {
        return template.getForObject("http://USER-SERVICE/api/user/v1/" + userId, UserResponse.class);
    }
}
