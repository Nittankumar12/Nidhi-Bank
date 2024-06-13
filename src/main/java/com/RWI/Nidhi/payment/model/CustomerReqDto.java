package com.RWI.Nidhi.payment.model;

import lombok.Data;

@Data
public class CustomerReqDto {
    private String customerName;
    private String email;
    private String phoneNumber;
    private String amount;
    private String orderId;
    private String paymentId;
}
