package com.RWI.Nidhi.user.serviceImplementation;
import com.RWI.Nidhi.entity.EmiDetails;
import org.springframework.stereotype.Service;

@Service
public class EmiService {

    public EmiDetails calculateEmi(double mrpPrice, double discountAmount, int durationMonths) {
        EmiDetails emiDetails = new EmiDetails();
        emiDetails.setMrpPrice(mrpPrice);
        emiDetails.setDiscount(discountAmount);

        // Calculate customer price after discount
        double customerPrice = mrpPrice - discountAmount;
        emiDetails.setCustomerPrice(customerPrice);

        // Calculate dealer price (5% off the customer price)
        double dealerPrice = customerPrice * 0.95;
        emiDetails.setDealerDiscount(dealerPrice);

        // Calculate deposit amount (3 months deposit)
        double deposit = customerPrice / 4;

        // Calculate remaining principal amount after deposit
        double principal = customerPrice - deposit;

        // Calculate total interest for the remaining principal amount
        double totalInterest = principal * 0.02 * durationMonths;

        // Calculate EMI based on duration (9 or 12 months)
        double emi = (principal + totalInterest) / durationMonths;

        if (durationMonths == 9) {
            emiDetails.setEmi9Months(emi);
        } else if (durationMonths == 12) {
            emiDetails.setEmi12Months(emi);
        } else {
            throw new IllegalArgumentException("Unsupported EMI duration: " + durationMonths);
        }

        return emiDetails;
    }
}
