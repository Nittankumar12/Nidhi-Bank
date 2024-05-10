package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.MisTenure;
import com.RWI.Nidhi.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MisRequestDto {
    private String userName;
    private int misId;
    private double totalDepositedAmount;
    private String nomineeName;
    private MisTenure misTenure;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String agentName;
}