package com.example.produceService.service;

import com.example.produceService.model.Asset;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ExcelUploadService {
    public static boolean isValidExcelFile(MultipartFile file){
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
    }
    public static List<Asset> getAssetsDataFromExcel(InputStream inputStream){
        List<Asset> assets = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("assets");
            int rowIndex =0;
            for (Row row : sheet){
                Date createdAt = new Date();
                if (rowIndex ==0){
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                Asset asset = new Asset();
                asset.setDateInStored(createdAt);

                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    switch (cellIndex){
                        case 0 -> asset.setAssetId(Math.round(cell.getNumericCellValue()));
                        case 1 -> asset.setAssetName(cell.getStringCellValue());
                        case 2 -> asset.setAssetStatus(cell.getStringCellValue());
                        case 3 -> asset.setAssetTypeId(Math.round(cell.getNumericCellValue()));
                        case 4 -> asset.setPrice(cell.getNumericCellValue());
                        case 5 -> asset.setUserUsedId(Math.round(cell.getNumericCellValue()));
                        case 6 -> asset.setDeptUsedId(Math.round(cell.getNumericCellValue()));

                        default -> {
                        }
                    }
                    cellIndex++;
                }
                assets.add(asset);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return assets;
    }

}