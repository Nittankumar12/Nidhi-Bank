package com.RWI.Nidhi.user.serviceImplementation;
import com.RWI.Nidhi.dto.LoanDto;
import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.entity.Scheme;
import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.user.repository.AccountsRepo;
import com.RWI.Nidhi.user.repository.LoanRepo;
import com.RWI.Nidhi.user.serviceInterface.UserLoanServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

public class UserLoanServiceImplementation implements UserLoanServiceInterface {
    @Autowired
    LoanRepo loanRepository;
    @Autowired
    AccountsRepo accountsRepo;
    @Autowired
    AccountsServiceImplementation accountsService;
    @Autowired
    SchemeServiceImplementation schemeService;

    public double maxApplicableLoan(int accountId) {
        double maxLoan = 0;
        if((accountsRepo.findSchemeListByAccountId(accountId) != null && accountsService.schemeRunning(accountId)) == Boolean.TRUE) {
            List<Scheme> currentScheme = accountsRepo.findSchemeListByAccountId(accountId);
            for (int i = 0; i < currentScheme.size(); i++) {
                Scheme sc = currentScheme.get(i);
                maxLoan =+ schemeService.findLoanOnSchemeBasis(sc.getSchemeId());
            }
        }
        else
            maxLoan = (accountsRepo.findCurrentBalanceByAccountId(accountId) * 10);
        return maxLoan;
    }
    @Override
    public void applyLoan(LoanDto loanDto) {// For User
        Loan currentLoan = new Loan();
        currentLoan.setLoanType(loanDto.getLoanType());
        currentLoan.setInterestRate(loanDto.getLoanType().getLoanInterestRate());
        currentLoan.setPrincipalLoanAmount(loanDto.getPrincipalLoanAmount());
        currentLoan.setRePaymentTerm(loanDto.getRePaymentTerm());
        currentLoan.setStartDate(loanDto.getStartDate());
        currentLoan.setStatus(LoanStatus.APPLIED);
        loanRepository.save(currentLoan);
    }
    @Override
    public LoanStatus checkLoanStatus(int loanId) {// For User
        return loanRepository.findStatusByLoanId(loanId);
    }
    @Override
    public int checkCurrentEMI(int loanId){
        int currentEMI = loanRepository.findEMIByLoanId(loanId);
        if(loanRepository.findFineByLoanId(loanId) != 0)
            currentEMI =+ loanRepository.findFineByLoanId(loanId);
        return currentEMI;
    }

    @Override
    public String payEMI(int loanId, int payedEMI) {
        int currentEMI = checkCurrentEMI(loanId);
        Loan currentLoan = loanRepository.findById(loanId).orElseThrow();
        if (loanRepository.findEMIByLoanId(loanId) == currentEMI) {
            currentLoan.setPayableLoanAmount(currentLoan.getPayableLoanAmount() - currentEMI);
            return "EMI payed";
        } else if (currentEMI != loanRepository.findEMIByLoanId(loanId)){
            return  "You have not entered the correct amount. " +
                    "You need to enter"+currentEMI+
                    ". Please enter the correct amount before due date";
        }
        else
            return "Invalid data";
    }
    @Override
    public void applyLoanClosure(int loanId){
        Loan currentLoan = loanRepository.findById(loanId).orElseThrow();
        currentLoan.setStatus(LoanStatus.REQUESTEDFORFORECLOSURE);
    }

}
