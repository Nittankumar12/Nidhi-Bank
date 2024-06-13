package com.RWI.Nidhi.payment.model;

public class Response {
    private int statusCode;
    private RazorPay razorPay;
    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public RazorPay getRazorPay() {
        return razorPay;
    }
    public void setRazorPay(RazorPay razorPay) {
        this.razorPay = razorPay;
    }

}
