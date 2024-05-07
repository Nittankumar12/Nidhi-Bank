package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.TransactionStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.Date;
@Data
public class TransactionsHistoryDto {
    private int transactionId;
    private String bankName;
    private double amount;
    private String accountNumber;
    private Date date;
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;


}
