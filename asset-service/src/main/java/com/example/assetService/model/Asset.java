package com.example.assetService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "Assets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asset {

    @Id
    @Column(name = "ASSET_ID")
    private Long assetId;
    @Column(name = "ASSET_NAME")
    private String assetName;
    @Column(name = "ASSET_STATUS")
    private Long assetStatus;
    @Column(name="SERIAL_NUMBER")
    private String serialNumber;
    @Column(name = "ASSET_TYPE")
    private Long assetType;
    @Column(name = "PRICE")
    private Double price;
    @Column(name = "DATE_IN_STORED")
    private Date dateInStored;
    @Column(name = "USER_USED_ID")
    private Long userUsedId;
    @Column(name = "DEPT_USED_ID")
    private Long deptUsedId;
    @Column(name = "DATE_USED")
    private Date dateUsed;
}
