package com.RWI.Nidhi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EmiDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private double mrpPrice;
    private double discount;
    private double dealerDiscount;
    private double customerPrice;
    private double emi9Months;
    private double emi12Months;

}