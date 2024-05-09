package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RdResponseDto {
    private String userName;
    private int RdId;
    private double monthlyDepositAmount;
    @Enumerated(EnumType.STRING)
    private Status rdStatus;
}
