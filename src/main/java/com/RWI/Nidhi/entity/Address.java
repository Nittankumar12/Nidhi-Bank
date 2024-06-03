package com.RWI.Nidhi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
@Entity
@Data
public class Address {
    @Id
    private Long id;
    private String address;
    private String district;
    private String state;
}
