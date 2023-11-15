package com.example.assetService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "asset_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetType {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String assetName;
    @Column(name = "asset_group_id")
    private int assetGroupId;
}
