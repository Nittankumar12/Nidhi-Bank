package com.RWI.Nidhi.payment.model;

public class RazorPay {
    private String razorpayOrderId;
    private String applicationFee;
    private String secretKey;
    private String paymentId;
    private String notes;
    private String imageURL;
    private String theme;
    private String  merchantName;
    private String purchaseDescription;
    private String customerName;
    private String customerEmail;
    private String customerContact;
    public RazorPay() {
        super();
        // TODO Auto-generated constructor stub
    }
    public RazorPay(String applicationFee, String razorpayOrderId, String secretKey, String notes,
                    String imageURL, String theme, String merchantName, String purchaseDescription, String customerName,
                    String customerEmail, String customerContact) {
        super();
        this.applicationFee = applicationFee;
        this.razorpayOrderId = razorpayOrderId;
        this.secretKey = secretKey;
//		this.paymentId = paymentId;
        this.notes = notes;
        this.imageURL = imageURL;
        this.theme = theme;
        this.merchantName = merchantName;
        this.purchaseDescription = purchaseDescription;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerContact = customerContact;
    }
    public String getApplicationFee() {
        return applicationFee;
    }

    public void setApplicationFee(String applicationFee) {
        this.applicationFee = applicationFee;
    }
    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }
    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }
    public String getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    public String getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getImageURL() {
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    public String getTheme() {
        return theme;
    }
    public void setTheme(String theme) {
        this.theme = theme;
    }
    public String getMerchantName() {
        return merchantName;
    }
    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
    public String getPurchaseDescription() {
        return purchaseDescription;
    }
    public void setPurchaseDescription(String purchaseDescription) {
        this.purchaseDescription = purchaseDescription;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getCustomerEmail() {
        return customerEmail;
    }
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    public String getCustomerContact() {
        return customerContact;
    }
    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    @Override
    public String toString() {
        return "RazorPay [applicationFee=" + applicationFee + ", razorpayOrderId=" + razorpayOrderId + ", secretKey="
                + secretKey + ", paymentId=" + paymentId + ", notes=" + notes + ", imageURL=" + imageURL + ", theme="
                + theme + ", merchantName=" + merchantName + ", purchaseDescription=" + purchaseDescription
                + ", customerName=" + customerName + ", customerEmail=" + customerEmail + ", customerContact="
                + customerContact + "]";
    }
}
