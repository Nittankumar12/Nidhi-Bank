package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.MisTenure;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public class MisDto {
    private double totalDepositedAmount;
    private String nomineeName;
//    @Enumerated(EnumType.STRING)
    private MisTenure misTenure;
}
