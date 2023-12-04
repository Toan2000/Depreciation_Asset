package com.example.depreciationService.mapping;

import com.example.depreciationService.client.DepreciationServiceClient;
import com.example.depreciationService.dto.request.DepreciationRequest;
import com.example.depreciationService.dto.response.*;
import com.example.depreciationService.model.Depreciation;
import com.example.depreciationService.model.DepreciationHistory;
import com.example.depreciationService.service.DepreciationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class DepreciationMapping {
    private final DepreciationServiceClient depreciationServiceClient;
    private final DepreciationHistoryService depreciationHistoryService;
    private final CommonMapping commonMapping;

    //Hàm nhận request và thực hiện tính khấu hao và lưu lịch sử
    public Depreciation requestToEntity(DepreciationRequest depreciationRequest, Object object) throws ParseException {
        Depreciation depreciation = new Depreciation();
        depreciation.setActive(true);
        depreciation.setStatus(1);
        depreciation.setDeptId(depreciationRequest.getDeptId());
        depreciation.setAssetId(depreciationRequest.getAssetId());
        depreciation.setFromDate(new Date());
        AssetResponse assetResponse = depreciationServiceClient.fetchAsset(depreciation.getAssetId());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        depreciation.setAssetTypeId(assetResponse.getAssetTypeId());
        depreciation.setExpDate(dateFormat.parse(assetResponse.getExpDate()));
        //Ngưng khấu hao đợt trước và gán khấu hao từ sau ngày kết thúc
        if(object != null){
            Date lDate = dateFormat.parse(((Object[])object)[1].toString());
            depreciation.setValuePerMonth(commonMapping.calculatorDepreciationPerMonth(assetResponse,Double.valueOf(((Object[])object)[2].toString()), dateFormat.format(lDate)));
            LocalDate localDate = lDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);
            depreciation.setFromDate(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            //Kiểm tra lịch sử khấu hao đã qua tháng mới chưa
            Date today = new Date();
            if(today.after(dateFormat.parse(localDate.getYear()+"-"+localDate.getMonthValue()+"-"+localDate.lengthOfMonth()))){
                DepreciationHistory depreciationHistory = new DepreciationHistory();
                depreciationHistory.setCreateAt(new Date());
                depreciationHistory.setMonth(localDate.getMonthValue());
                depreciationHistory.setYear(today.getYear());
                depreciationHistory.setDepreciation(depreciation);
                depreciationHistory.setAssetId(depreciation.getAssetId());
                depreciationHistory.setAssetTypeId(depreciation.getAssetTypeId());
                depreciationHistory.setValue((localDate.lengthOfMonth()-localDate.getDayOfMonth()+1)*depreciation.getValuePerMonth());
                depreciationHistoryService.saveDepreciationHistory(depreciationHistory);
            }
            //Kéo lịch sử khấu hao
            int month = depreciation.getFromDate().getMonth()+1;
            for (int i = depreciation.getFromDate().getYear(); i <=today.getYear(); i++){
                for(int j = month; j<12;j++){
                    if(j==today.getMonth()&&i==today.getYear())
                        break;
                    DepreciationHistory depreciationHistory = new DepreciationHistory();
                    depreciationHistory.setCreateAt(new Date());
                    depreciationHistory.setMonth(localDate.getMonthValue());
                    depreciationHistory.setYear(today.getYear());
                    depreciationHistory.setDepreciation(depreciation);
                    depreciationHistory.setAssetId(depreciation.getAssetId());
                    depreciationHistory.setAssetTypeId(depreciation.getAssetTypeId());
                    depreciationHistory.setValue(depreciation.getValuePerMonth());
                    depreciationHistoryService.saveDepreciationHistory(depreciationHistory);
                }

            }
        }
        else depreciation.setValuePerMonth(assetResponse.getPrice()/assetResponse.getAmountOfYear());
        depreciation.setUserId(depreciationRequest.getUserId());
        depreciation.setCreateAt(new Date());
        return depreciation;
    }

//    public DepreciationResponse EntityToResponse(Depreciation depreciation){
//        DepreciationResponse depreciationResponse = new DepreciationResponse();
//        AssetResponse assetResponse = depreciationServiceClient.fetchAsset(depreciation.getAssetId());
//        depreciationResponse.setAssetResponse(assetResponse);
//        depreciationResponse.setId(depreciation.getId());
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        depreciationResponse.setFromDate(dateFormat.format(depreciation.getFromDate()));
//        if(depreciation.getToDate()==null){
//            depreciationResponse.setToDate(null);
//            depreciationResponse.setAmountMonth(new Date().getMonth() - depreciation.getFromDate().getMonth());
//            depreciationResponse.setValueDepreciation(commonMapping.calculatorDepreciation(assetResponse, dateFormat.format(depreciation.getFromDate()),dateFormat.format(new Date()), ));
////            depreciationResponse.setValueDepreciation(depreciationServiceClient.getDepreciationValue(depreciation.getAssetId(),dateFormat.format(depreciation.getFromDate()) ,dateFormat.format(new Date())));
//        }
//        else{
//            depreciationResponse.setToDate(dateFormat.format(depreciation.getToDate()));
//            depreciationResponse.setAmountMonth(depreciation.getToDate().getMonth() - depreciation.getFromDate().getMonth());
//            depreciationResponse.setValueDepreciation(depreciation.getValueDepreciation());
//        }
//        depreciationResponse.setCreateAt(dateFormat.format(depreciation.getCreateAt()));
//        depreciationResponse.setActive(depreciation.isActive());
//        depreciationResponse.setUserResponse(depreciationServiceClient.fetchUser(depreciation.getUserId()));
//        return depreciationResponse;
//    }

    public Depreciation updateDepreciation(Depreciation depreciation){
        Date endDate = new Date();
        depreciation.setToDate(endDate);
        depreciation.setAmountMonth(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        depreciation.setValueDepreciation(depreciationServiceClient.getDepreciationValue(depreciation.getAssetId(),dateFormat.format(depreciation.getFromDate()), dateFormat.format(new Date())));
        depreciation.setStatus(2);
        return depreciation;
    }

    public DepreciationByAssetResponse getDepreciationAssetResponse(Long assetId, List<Depreciation> lDepreciation) throws ParseException {
        DepreciationByAssetResponse depreciationByAssetResponse = new DepreciationByAssetResponse();
        LocalDate localDate = LocalDate.now();
        int amountDate = 0;
        Double valuePre = 0.0;
        Double valuePrev = depreciationHistoryService.totalValueDepreciationByAssetId(assetId,new Date().getMonth()+1, new Date().getYear()+1900);
        //Khởi tạo ngày đầu tháng và hôm nay
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date sDate = dateFormat.parse(localDate.getYear()+"-"+localDate.getMonthValue()+"-01");
        Date today = new Date();
        AssetResponse assetResponse = depreciationServiceClient.fetchAsset(assetId);
        Date expDate = dateFormat.parse(assetResponse.getExpDate());
        List<DepreciationByAssetResponse.DepreciationAssetHistory> list = new ArrayList<>();
        for(Depreciation depreciation: lDepreciation){
            List<DepreciationHistoryByDepreciation> depreciationList = getDepreciationHistoryByDepreciation(depreciation);
            UserResponse userResponse = depreciationServiceClient.fetchUser(depreciation.getUserId());
            Double value = 0.0;
            if(depreciation.getToDate() == null){
                Object object = depreciationHistoryService.getValueHistoryByDepreciation(localDate.getMonthValue(), localDate.getYear(), depreciation.getId());
                value += object != null ? Double.valueOf(((Object[])object)[1].toString()): 0.0;
                //Kiểm tra tài sản còn khấu hao hay không
                if(expDate.after(sDate)&&expDate.before(today)){
                    //Khấu hao được tính từ ngày đầu đến ngày kết thúc
                    amountDate = expDate.getDate()-sDate.getDate()+1;
                    valuePre = (Double.valueOf(amountDate)/localDate.lengthOfMonth())*depreciation.getValuePerMonth();
                }else if(expDate.before(sDate)){
                    //Khấu hao đã kết thúc
                    valuePre = 0.0;
                    amountDate = 0;
                }else {
                    //Khấu hao vẫn chưa hết
                    amountDate = today.getDate()-sDate.getDate()+1;
                    valuePre = (Double.valueOf(amountDate)/localDate.lengthOfMonth())*depreciation.getValuePerMonth();
                }
                value+=valuePre;
            }
            String toDate;
            if(depreciation.getToDate()==null&&depreciation.getExpDate().before(sDate))
                toDate = "Tài sản đã kết thúc khấu hao";
            else if(depreciation.getToDate()==null)
                toDate = "Đang sử dụng";
            else
                toDate = dateFormat.format(depreciation.getToDate());
            list.add(new DepreciationByAssetResponse.DepreciationAssetHistory(depreciation.getId()
                    ,userResponse
                    ,dateFormat.format(depreciation.getFromDate())
                    ,toDate
                    ,depreciation.getValueDepreciation()==null?value: depreciation.getValueDepreciation()
                    ,0
                    ,depreciationList));
        }
        //Tạo thông tin Response
        depreciationByAssetResponse.setValuePre(valuePre);
        depreciationByAssetResponse.setValuePrev(valuePrev);
        depreciationByAssetResponse.setLengthOfMonth(localDate.lengthOfMonth());
        depreciationByAssetResponse.setAmountDate(amountDate);
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
    public List<DepreciationHistoryByDepreciation> getDepreciationHistoryByDepreciation(Depreciation depreciation){
        List<DepreciationHistoryByDepreciation> list = new ArrayList<>();
        List<DepreciationHistory> depreciationHistories = depreciationHistoryService.findByDepreciation(depreciation);
        for(DepreciationHistory depreciationHistory : depreciationHistories){
            DepreciationHistoryByDepreciation depreciationHistoryByDepreciation  = list.stream()
                    .filter(o -> o.getYear() == depreciationHistory.getYear())
                    .findFirst()
                    .orElse(null);
            if(depreciationHistoryByDepreciation == null){
                depreciationHistoryByDepreciation = new DepreciationHistoryByDepreciation();
                depreciationHistoryByDepreciation.setYear(depreciationHistory.getYear());
                Map<String,Double> months = new HashMap<>();
                months.put(String.valueOf(depreciationHistory.getMonth()),depreciationHistory.getValue());
                depreciationHistoryByDepreciation.setMonths(months);
                list.add(depreciationHistoryByDepreciation);
            }else{
                Map<String,Double> months = depreciationHistoryByDepreciation.getMonths();
                months.put(String.valueOf(depreciationHistory.getMonth()),depreciationHistory.getValue());
                depreciationHistoryByDepreciation.setMonths(months);
            }
        }
        return list;
    }

}
