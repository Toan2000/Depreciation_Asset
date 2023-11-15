package com.example.produceService.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

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
    @JsonIgnore
    @OneToMany(mappedBy = "assetType",cascade = CascadeType.ALL)
    private List<Asset> listAsset;
}
