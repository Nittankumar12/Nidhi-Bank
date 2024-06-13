package com.RWI.Nidhi.payment.model;

import com.RWI.Nidhi.entity.Transactions;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Customer {
    @Id
    private int cId;
    private String customerName;
    private String email;
    private String phoneNumber;
    private String amount;
    private String orderId;
    private String paymentId;
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Transactions transaction;
}