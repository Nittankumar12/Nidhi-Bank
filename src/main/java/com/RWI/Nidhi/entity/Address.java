package com.RWI.Nidhi.entity;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
public class Address {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String address;
    private String district;
    private String state;
    @OneToOne
    @JoinColumn
    KycDetails kycDetails;
}
