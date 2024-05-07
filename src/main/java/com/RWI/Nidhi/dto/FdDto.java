package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.FdCompoundingFrequency;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;


@Data
public class FdDto {
    int amount;
    private String nomineeName;
    @Enumerated(EnumType.STRING)
    FdCompoundingFrequency fdCompoundingFrequency;
    int tenure;
}
