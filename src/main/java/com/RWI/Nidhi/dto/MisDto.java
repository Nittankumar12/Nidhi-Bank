package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.MisTenure;
import lombok.Getter;

@Getter
public class MisDto {
    private String userName;
    private double totalDepositedAmount;
    private String nomineeName;
//    @Enumerated(EnumType.STRING)
    private MisTenure misTenure;
    private String agentName;
}
