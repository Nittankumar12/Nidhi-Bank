package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.MisTenure;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MisDto {
    private double totalDepositedAmount;
    private String nomineeName;
//    @Enumerated(EnumType.STRING)
    private MisTenure misTenure;
}
