package com.RWI.Nidhi.dto;


import com.RWI.Nidhi.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FdRequestDto {
    private String userName;
    private int fdId;
    private int amount;
    int tenure;
    private String nomineeName;
    @Enumerated(EnumType.STRING)
    private Status fdStatus;
    private String agentName;
}
