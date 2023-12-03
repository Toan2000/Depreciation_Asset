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
        //Ngưng khấu hao đợt trước và gán khấu hao từ sau ngày kết thúc
        if(object != null){
            Date lDate = dateFormat.parse(((Object[])object)[1].toString());
            depreciation.setValuePerMonth(calculatorDepreciationPerMonth(assetResponse,Double.valueOf(((Object[])object)[2].toString()), dateFormat.format(lDate)));
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
            List<DepreciationHistoryByDepreciation> depreciationList = getDepreciationHistoryByDepreciation(depreciation);
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
                    ,amountDate
                    ,depreciationList));
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

    public Double calculatorDepreciationPerMonth(AssetResponse asset, Double value, String lastDate) throws ParseException {
        //Lấy thông tin tài sản và thời gian
        Date lDate = new SimpleDateFormat("yyyy-MM-dd").parse(lastDate);
        Date eDate = new SimpleDateFormat("yyyy-MM-dd").parse(asset.getExpDate());
        //Tính số ngày trong tháng
        int daysInMonthLDate = LocalDate.from(lDate.toInstant().atZone(ZoneId.systemDefault())).lengthOfMonth();
        int daysInMonthEDate = LocalDate.from(eDate.toInstant().atZone(ZoneId.systemDefault())).lengthOfMonth();
        //Tính số tháng còn lại
        int amountMonth = (lDate.getDate() >= daysInMonthLDate/2 ? 0 : 1)
                + (11 - lDate.getMonth())
                + (eDate.getYear() - lDate.getYear() -1)*12
                + (eDate.getMonth())
                + (eDate.getDate() > daysInMonthEDate/2 ? 1: 0);
        //Kiểm tra tài sản có nâng cấp hay không
        if(asset.getUpdateId()!=null){
            return (asset.getPrice() - value)/amountMonth;
        }
        return asset.getPrice()/asset.getAmountOfYear();
    }
    public Double calculatorDepreciation(AssetResponse asset, String fromDate, String toDate, Double value, String lastDate) throws ParseException {
        //Lấy thông tin tài sản và thời gian
        Date fDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
        Date tDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
        Date lDate = new SimpleDateFormat("yyyy-MM-dd").parse(lastDate);
        Date eDate = new SimpleDateFormat("yyyy-MM-dd").parse(asset.getExpDate());
        int daysInMonth = LocalDate.from(fDate.toInstant().atZone(ZoneId.systemDefault())).lengthOfMonth();
        int daysInLMonth = LocalDate.from(lDate.toInstant().atZone(ZoneId.systemDefault())).lengthOfMonth();
        int daysInEMonth = LocalDate.from(eDate.toInstant().atZone(ZoneId.systemDefault())).lengthOfMonth();
        int amountMonth = (lDate.getDate() >= daysInLMonth/2 ? 0 : 1)
                + (11 - lDate.getMonth())
                + (eDate.getYear() - lDate.getYear() -1)*12
                + (eDate.getMonth())
                + (eDate.getDate() > daysInEMonth/2 ? 1: 0);
        //Kiểm tra thông tin là tháng cuối hay chưa
        if(eDate.getMonth()==fDate.getMonth()&&eDate.getYear()==fDate.getYear())
            return depreciation3(asset.getPrice(),value,amountMonth);
        //Kiểm tra tài sản có nâng cấp hay không
        if(asset.getUpdateId()!=null){
            return depreciation2(asset.getPrice(),value,Long.valueOf(amountMonth),tDate.getDate()-fDate.getDate()+1,daysInMonth);
        }
        return depreciation1(asset.getPrice(), asset.getAmountOfYear(),tDate.getDate()-fDate.getDate()+1,daysInMonth);
    }

    //Công thức tính khấu hao 1
    public Double depreciation1(Double price, int amountMonth, int days, int amountDay) {
        return (price/amountMonth)*(Double.valueOf(days)/amountDay);
    }

    //Công thức tính khấu hao 2
    public Double depreciation2(Double price, Double valueUsed, Long amountMonth, int days, int amountDay){
        return ((price - valueUsed)/amountMonth)*(Double.valueOf(days)/amountDay);
    }
    //Công thức tính khấu hao 3
    public Double depreciation3(Double price, Double valueUsed,int amountMonth){
        return price - valueUsed - (amountMonth-1)*((price-valueUsed)/amountMonth);
    }

}
