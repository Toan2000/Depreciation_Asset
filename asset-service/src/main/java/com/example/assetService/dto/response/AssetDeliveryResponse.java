package com.example.assetService.dto.response;

import lombok.Data;

import java.util.List;
@Data
public class AssetDeliveryResponse {
    private Long storageId;
    private String storageName;
    private String storageLocation;
    private UserResponse userResponse;
    private String dateInStored;
    private String dateUsed;
    private List<DeliveryHistory> deliveryHistories;
    private List<DeliveryHistory> recallHistories;
    private List<DeliveryHistory> brokenHistories;
    @Data
    public static class DeliveryHistory{
        private UserResponse userResponse;
        private String deliveryDate;
        private int status;
        private String deliveryType;
        private String note;
    }
}
