package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.TransactionStatus;
import com.RWI.Nidhi.payment.model.Customer;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
@Data
public class TransactionsHistoryDto {
    private int transactionId;
    private String bankName;
    private double amount;
    private String accountNumber;
    private LocalDate date;
    private Customer customer;
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;
}
