package com.example.depreciationService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "depreciation")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Depreciation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "asset_id")
    private Long assetId;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "dept_id")
    private Long deptId;
    @Column(name = "from_date")
    private Date fromDate;
    @Column(name = "to_date")
    private Date toDate;
    @Column(name = "amount_month")
    private int amountMonth;
    @Column(name = "value_depreciation")
    private Long valueDepreciation;
    @Column(name = "create_at")
    private Date createAt;
    @Column(name = "active")
    private boolean active;
    @Column(name = "status")
    private int status;
}
