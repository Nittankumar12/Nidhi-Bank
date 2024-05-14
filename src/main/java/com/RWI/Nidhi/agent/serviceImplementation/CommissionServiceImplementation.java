package com.RWI.Nidhi.agent.serviceImplementation;

import com.RWI.Nidhi.agent.serviceInterface.CommissionService;
import com.RWI.Nidhi.entity.Commission;
import com.RWI.Nidhi.entity.FixedDeposit;
import com.RWI.Nidhi.entity.MIS;
import com.RWI.Nidhi.entity.RecurringDeposit;
import com.RWI.Nidhi.repository.CommissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommissionServiceImplementation implements CommissionService {

    @Autowired
    private CommissionRepository commissionRepository;


    @Override
    public void calculateCommission(Commission commission) {
        if (commission.getFixedDeposit() != null) {
            calculateFixedDepositCommission(commission);
        } else if (commission.getRecurringDeposit() != null) {
            calculateRecurringDepositCommission(commission);
        } else if (commission.getMonthlyIncomeScheme() != null) {
            calculateMonthlyIncomeSchemeCommission(commission);
        }
    }

    private void calculateFixedDepositCommission(Commission commission) {
        FixedDeposit fixedDeposit = commission.getFixedDeposit();
        double commissionAmount = fixedDeposit.getAmount() * commission.getCommissionRate();
        commission.setCommissionAmount(commissionAmount);
        commissionRepository.save(commission);
    }

    private void calculateRecurringDepositCommission(Commission commission) {
        RecurringDeposit recurringDeposit = commission.getRecurringDeposit();
        double commissionAmount = recurringDeposit.getMonthlyDepositAmount() * commission.getCommissionRate();
        commission.setCommissionAmount(commissionAmount);
        commissionRepository.save(commission);
    }

    private void calculateMonthlyIncomeSchemeCommission(Commission commission) {
        MIS monthlyIncomeScheme = commission.getMonthlyIncomeScheme();
        double commissionAmount = monthlyIncomeScheme.getTotalDepositedAmount() * commission.getCommissionRate();
        commission.setCommissionAmount(commissionAmount);
        commissionRepository.save(commission);
    }



//    public Commission createCommission(Commission commission) {
//        commission.setCommissionAmount(calculateCommission(commission));
//        return commissionRepository.save(commission);
//    }
//
//    private Double calculateCommission(Commission commission) {
//        Double commissionAmount = 0.0;
//
//        switch (commission.getProductType()) {
//            case "fixed_deposit":
//                commissionAmount = commission.getInvestmentAmount() * commission.getCommissionRate();
//                break;
//            case "recurring_deposit":
//                commissionAmount = commission.getInvestmentAmount() * commission.getCommissionRate() * commission.getInvestmentTerm();
//                break;
//            case "monthly_income_scheme":
//                commissionAmount = commission.getInvestmentAmount() * commission.getCommissionRate() * (commission.getInvestmentTerm() / 12.0);
//                break;
//            default:
//                throw new RuntimeException("Invalid product type");
//        }
//
//        return commissionAmount;
//    }
}
