package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.TransactionStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;
@Data
public class UserTransactionsHistoryDto {
    private int transactionId;
    private double amount;
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;
    private String transactionCause;
}
