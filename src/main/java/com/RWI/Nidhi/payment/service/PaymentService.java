package com.RWI.Nidhi.payment.service;

import com.RWI.Nidhi.payment.model.Customer;
import com.RWI.Nidhi.payment.model.RazorPay;
import com.RWI.Nidhi.payment.model.Response;
import com.google.gson.Gson;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    private RazorpayClient client;
    private static Gson gson = new Gson();

    private static final String KEY_ID = "rzp_test_CPpsnUBeLXLVlP";
    private static final String SECRET_KEY = "VxR0aRIJPd1cY1h1mS2ttPpB";

    public PaymentService() throws RazorpayException {
        this.client = new RazorpayClient(KEY_ID, SECRET_KEY);
    }


    public ResponseEntity<String> createOrder(Customer customer) {
        try {
            String orderId = createRazorPayOrder(customer.getAmount());
            System.out.println("Order Created");
            customer.setOrderId(orderId);
            RazorPay razorPay = getRazorPay(orderId, customer);
            System.out.println("Created the RazorPay");
            System.out.println("Order ID is: " + customer.getOrderId());

            // Fetching payment ID
            List<Payment> payments = client.Orders.fetchPayments(orderId);
            if (!payments.isEmpty()) {
                customer.setPaymentId(payments.get(0).get("id"));
            }
            System.out.println(customer.getPaymentId());
            // Creating a response object
            Response response = new Response();
            response.setStatusCode(HttpStatus.OK.value());
            response.setRazorPay(razorPay);

            return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
        } catch (RazorpayException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to create payment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Payment>> fetchPayments(String orderId){
        try{
            List<Payment> payments = client.Orders.fetchPayments(orderId);
            if (!payments.isEmpty()) {
                return new ResponseEntity<>(payments,HttpStatus.OK);
//                 Get the first payment (assuming there could be only one payment per order)
//
//                Payment payment = payments.get(0);
//                System.out.println( " payId: " + payment.get("id"));
//                System.out.println( " amount: " + payment.get("amount"));
//                System.out.println( " status: " + payment.get("status"));
//                System.out.println( " mode: " + payment.get("method"));
//                System.out.println( " emailId: " + payment.get("email"));
//                System.out.println( " contactNo: " + payment.get("contact"));
//                System.out.println( " appFee: " + payment.get("fee"));
//                System.out.println( " entity: " + payment.get("entity"));


//                return ResponseEntity.ok("Order marked as paid");
            } else {
                // Handle the case when no payments are associated with the order
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> markAsAttempted(String orderId) {
        try {
            // Update order status as attempted in your system
            // Return success response
            Order order = client.Orders.fetch(orderId);
            System.out.println("order id:  " +  order.get("id").toString());
            System.out.println("amountPaid:  " +  order.get("amount_paid").toString());
            System.out.println("createdAt  " +  order.get("created_at").toString());
            System.out.println("status  " +  order.get("status").toString());
            System.out.println("amount:  " +  order.get("amount").toString());
            System.out.println("amount_due: " + order.get("amount_due").toString());
            System.out.println("amount_paid: " + order.get("amount_paid").toString());
            return ResponseEntity.ok("Order marked as attempted");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to mark order as attempted", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> markAsPaid(String orderId,Customer customer) {
        try {
            // Update order status as paid in your system
            // Return success response
            List<Payment> payments = client.Orders.fetchPayments(orderId);

            if (!payments.isEmpty()) {
//                customer.setPaymentId(payments.get(0).get("id"));
                return ResponseEntity.ok("Order marked as paid");
            } else {
                // Handle the case when no payments are associated with the order
                return new ResponseEntity<>("No payments found for the specified order ID", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to mark order as paid", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> verifyPayment(String orderId) {
        try {
            // Mock payment verification logic
            Order order  = client.Orders.fetch(orderId);
            if(order.get("status").equals("paid")) return new ResponseEntity<>(gson.toJson("paid"),HttpStatus.OK);
            else if(order.get("status").equals("attempted")) return new ResponseEntity<>(gson.toJson("pending"),HttpStatus.OK);
            else return new ResponseEntity<>(gson.toJson("order created"),HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("OrderId doesn't exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> generateQRCode(String orderId) {
        try {
            String qrCodeUrl = generateQRCodeUrl(orderId);
            return new ResponseEntity<>(qrCodeUrl, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to generate QR code", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private String generateQRCodeUrl(String orderId) {
        // Logic to generate QR code URL for the given order ID
        return "" + orderId;
    }

    private Response getResponse(RazorPay razorPay, int statusCode) {
        Response response = new Response();
        response.setStatusCode(statusCode);
        response.setRazorPay(razorPay);
        return response;
    }

    private RazorPay getRazorPay(String orderId, Customer customer) throws RazorpayException {
        RazorPay razorPay = new RazorPay();
//        razorPay.setPaymentId(paymentId); // Set the paymentId
        razorPay.setRazorpayOrderId(orderId); // Set the orderId
        razorPay.setApplicationFee(convertRupeeToPaise(customer.getAmount()));
        razorPay.setCustomerName(customer.getCustomerName());
        razorPay.setCustomerEmail(customer.getEmail());
        razorPay.setMerchantName("Test");
        razorPay.setPurchaseDescription("TEST PURCHASES");
        razorPay.setSecretKey(KEY_ID);
        razorPay.setImageURL("/logo");
        razorPay.setTheme("#F37254");
        razorPay.setNotes("notes" + orderId);

//         Assuming you have a method to capture payment, after creating the order
//        JSONObject captureObject = new JSONObject();
//        captureObject.put("amount", customer.getAmount()); // Amount to capture
//        captureObject.put("currency","INR");
//        Payment captureResponse = client.Payments.capture(orderId,captureObject);
// Retrieve the payment ID from the capture response
//        String capturedPaymentId = captureResponse.get("id");
//        razorPay.setPaymentId(capturedPaymentId);

//        if (captureResponse != null && captureResponse.get("id") != "") {
//            // Retrieve the payment ID from the capture response
//            System.out.println("Getting id: ");
//            String capturedPaymentId = captureResponse.get("id");
//            System.out.println("getted id");
//            razorPay.setPaymentId(capturedPaymentId);
//        } else {
//            System.out.println("abe galat hogya");
//        }

        return razorPay;
    }

    private String createRazorPayOrder(String amount) throws RazorpayException {
        JSONObject options = new JSONObject();
        options.put("amount", convertRupeeToPaise(amount));
        options.put("currency", "INR");
        options.put("receipt", "txn_123456");
//        options.put("payment_capture", 1); // Auto Capture enabled
        Order order = client.Orders.create(options);
        System.out.println("order id:  " +  order.get("id").toString());
        System.out.println("amountPaid:  " +  order.get("amount_paid").toString());
        System.out.println("entitYY  " +  order.get("entity").toString());
        System.out.println("status  " +  order.get("status").toString());
        System.out.println("amonttt:  " +  order.get("amount").toString());

        return order.get("id");
    }

    private String convertRupeeToPaise(String paise) {
        BigDecimal b = new BigDecimal(paise);
        BigDecimal value = b.multiply(new BigDecimal("100"));
        return value.setScale(0,BigDecimal.ROUND_UP).toString();
    }

}
