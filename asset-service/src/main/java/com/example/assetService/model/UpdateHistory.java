package com.example.assetService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "update_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateHistory {
    @Id
    @Column(name = "update_history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name = "value")
    private Double value;
    @Column(name="date_update")
    private Date dateUpdate;
    @Column(name="create_at")
    private Date createAt;

}
