package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FdResponseDto {
    private String userName;
    private int fdId;
    private int amount;
    @Enumerated(EnumType.STRING)
    private Status fdStatus;
}
