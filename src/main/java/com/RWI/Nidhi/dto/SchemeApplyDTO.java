package com.RWI.Nidhi.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class SchemeApplyDTO {
    private String email;
    private int tenure;
    private double schemeAmount;
}
