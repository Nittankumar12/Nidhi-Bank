package com.RWI.Nidhi.entity;

import com.RWI.Nidhi.enums.CommissionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Commission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Double commissionAmount;
    private double commissionRate;
    @Enumerated(EnumType.STRING)
    private CommissionType commissionType;//Commission value - laon, mis, etc.
    private Date commDate;
    @ManyToOne
    @JoinColumn
    User user;
    @ManyToOne
    @JoinColumn
    private Agent agent;


}
