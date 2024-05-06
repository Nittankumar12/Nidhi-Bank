package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.FdCompoundingFrequency;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public class FdDto {
    int amount;
    private String nomineeName;
    @Enumerated(EnumType.STRING)
    FdCompoundingFrequency fdCompoundingFrequency;
    int tenure;
}
