package com.RWI.Nidhi.payment.controller;

import com.RWI.Nidhi.payment.model.Customer;
import com.RWI.Nidhi.payment.model.RazorPay;
import com.RWI.Nidhi.payment.model.Response;
import com.RWI.Nidhi.payment.service.PaymentService;
import com.google.gson.Gson;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api")

public class PaymentController {
    @Autowired
    PaymentService paymentService;
    @PostMapping("/createOrder")
    public ResponseEntity<String> createOrder(@RequestBody Customer customer) {
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