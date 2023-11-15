package com.example.produceService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
    private String assetStatus;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ASSET_TYPE_ID")
    private AssetType assetType;
    @JsonIgnore
    @OneToMany(mappedBy = "asset",cascade = CascadeType.ALL)
    private List<Accessary> listAccessary;
}
