package com.example.depreciationService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "depreciation_history")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DepreciationHistory {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "create_at")
    private Date createAt;
    @Column(name= "month")
    private int month;
    @Column(name="year")
    private int year;
    @Column(name ="value")
    private Double value;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "\"depreciation_id\"")
    private Depreciation depreciation;
}
