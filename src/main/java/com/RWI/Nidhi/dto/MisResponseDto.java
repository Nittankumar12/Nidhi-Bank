package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MisResponseDto {
    private int misId;
    private double totalDepositedAmount;
    @Enumerated(EnumType.STRING)
    private Status status;
}