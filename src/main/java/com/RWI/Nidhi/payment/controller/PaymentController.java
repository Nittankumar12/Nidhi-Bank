package com.RWI.Nidhi.payment.controller;

import com.RWI.Nidhi.payment.model.Customer;
import com.RWI.Nidhi.payment.model.CustomerReqDto;
import com.RWI.Nidhi.payment.service.PaymentService;
import com.razorpay.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    PaymentService paymentService;
    @PostMapping("/createOrder")
    public ResponseEntity<String> createOrder(@RequestBody CustomerReqDto customerReqDto) {
        Customer customer = new Customer();

        customer.setOrderId(customerReqDto.getOrderId());
        customer.setPaymentId(customerReqDto.getPaymentId());
        customer.setCustomerName(customerReqDto.getCustomerName());
        customer.setAmount(customerReqDto.getAmount());
        customer.setEmail(customerReqDto.getEmail());
        customer.setPhoneNumber(customerReqDto.getPhoneNumber());
        return paymentService.createOrder(customer);
    }
    @PostMapping("/fetch")
    public ResponseEntity<List<Payment>> fetchPayments(@RequestParam String orderId) {
        return paymentService.fetchPayments(orderId);
    }
    @PostMapping("/attempted")
    public ResponseEntity<String> markAsAttempted(@RequestParam String orderId) {
        return paymentService.markAsAttempted(orderId);
    }
    @PostMapping("/paid")
    public ResponseEntity<String> markAsPaid(@RequestParam String orderId, @RequestBody Customer customer) {
        return paymentService.markAsPaid(orderId, customer);
    }
    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestParam String orderId) {
        return paymentService.verifyPayment(orderId);
    }
    @PostMapping("/genQRCode")
    public ResponseEntity<String> generateQRCode(@RequestParam String orderId) {
        return paymentService.generateQRCode(orderId);
    }
}