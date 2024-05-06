package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.*;

import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.Loan;

import com.RWI.Nidhi.entity.Penalty;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.LoanStatus;

import com.RWI.Nidhi.enums.PenaltyStatus;
import com.RWI.Nidhi.repository.AccountsRepo;
import com.RWI.Nidhi.repository.LoanRepo;
import com.RWI.Nidhi.repository.PenaltyRepo;
import com.RWI.Nidhi.repository.UserRepo;
import com.RWI.Nidhi.user.serviceInterface.UserLoanServiceInterface;
import com.RWI.Nidhi.user.serviceInterface.UserService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class UserLoanServiceImplementation implements UserLoanServiceInterface {


    @Autowired
    LoanRepo loanRepository;
    @Autowired
    UserService userService;
    @Autowired
    AccountsRepo accountsRepo;
    @Autowired
    UserPenaltyServiceImplementation penaltyService;
    @Autowired
    UserRepo userRepo;
    @Autowired
    PenaltyRepo penaltyRepo;

    @Override
    public double maxApplicableLoan(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        return acc.getCurrentBalance() * 5;
    }

    @Override
    public void applyLoan(LoanApplyDto loanApplyDto) {// For User

        User user = userService.getByEmail(loanApplyDto.getEmail());
        Accounts acc = user.getAccounts();

        LoanCalcDto loanCalcDto = new LoanCalcDto();
        loanCalcDto.setLoanType(loanApplyDto.getLoanType());
        loanCalcDto.setRePaymentTerm(loanApplyDto.getRePaymentTerm());
        loanCalcDto.setPrincipalLoanAmount(loanApplyDto.getPrincipalLoanAmount());
        loanCalcDto.setInterestRate(loanApplyDto.getLoanType());

        Loan loan = new Loan();
        loan.setLoanType(loanApplyDto.getLoanType());
        loan.setInterestRate(loanApplyDto.getLoanType().getLoanInterestRate());
        loan.setRePaymentTerm(loanApplyDto.getRePaymentTerm());
        loan.setPrincipalLoanAmount(loanApplyDto.getPrincipalLoanAmount());
        loan.setStartDate(LocalDate.now());
        loan.setEmiDate(calcFirstEMIDate(loan.getStartDate()));
        //Payable
        loan.setPayableLoanAmount(calculateFirstPayableAmount(loanCalcDto));
        //MonthlyEMI
        loan.setMonthlyEMI(calculateEMI(loanCalcDto));
        //Status
        loan.setStatus(LoanStatus.APPLIED);
        loan.setAccount(acc);// save acc in loan
        List<Loan> loanList = new ArrayList<>();
        loanList.add(loan);
        acc.setLoanList(loanList);// save loan in acc
        user.setAccounts(acc);
        loanRepository.save(loan);//save loan in loan
        accountsRepo.save(acc);
        userRepo.save(user);
    }

    @Override
    public LocalDate calcFirstEMIDate(LocalDate startDate) {
        return firstDateOfNextMonth(startDate);
    }

    @Override
    public Boolean checkForExistingLoan(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        Boolean b = Boolean.FALSE;
        List<Loan> loanList = acc.getLoanList();
        if(loanList.isEmpty() == Boolean.TRUE){
            return Boolean.TRUE;
        }
        for (int i = 0; i < loanList.size(); i++) {
            if (loanList.get(i).getStatus() == LoanStatus.CLOSED || loanList.get(i).getStatus() == LoanStatus.FORECLOSED || loanList.get(i).getStatus() == LoanStatus.REJECTED){
                b = Boolean.TRUE;
            }
            else{
                b = Boolean.FALSE;
            }
        }
        return b;
    }

    @Override
    public Boolean checkForLoanBound(String email, double principalLoanAmount) {
        double maxLoan = maxApplicableLoan(email);
        if (principalLoanAmount > maxLoan)
            return Boolean.FALSE;
        else
            return Boolean.TRUE;
    }

    public double calculateFirstPayableAmount(LoanCalcDto loanCalcDto) {
        //Internal Methods for apply Loan, only to be used when initially
        double p = loanCalcDto.getPrincipalLoanAmount();
        double r = loanCalcDto.getLoanType().getLoanInterestRate()/100;
        int n = loanCalcDto.getRePaymentTerm();
        loanCalcDto.setPayableLoanAmount(p * r * n * (Math.pow((1 + r), n)) / ((Math.pow((1 + r), n)) - 1));
        return loanCalcDto.getPayableLoanAmount();
    }

    @Override
    public double calculateEMI(LoanCalcDto loanCalcDto) {
        //Internal Methods for apply Loan
        double p = loanCalcDto.getPrincipalLoanAmount();
        double r = loanCalcDto.getLoanType().getLoanInterestRate()/100;
        int n = loanCalcDto.getRePaymentTerm();
        loanCalcDto.setMonthlyEMI(p * r * (Math.pow((1 + r), n)) / ((Math.pow((1 + r), n)) - 1));
        return loanCalcDto.getMonthlyEMI();
    }

    @Override
    public LoanInfoDto getLoanInfo(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        LoanInfoDto loanInfoDto = new LoanInfoDto();
        List<Loan> loanList = acc.getLoanList();
        for (int i = 0; i < loanList.size(); i++) {
            if (checkForExistingLoan(email) == Boolean.FALSE) {
                loanInfoDto.setLoanType(loanList.get(i).getLoanType());
                loanInfoDto.setPrincipalLoanAmount(loanList.get(i).getPrincipalLoanAmount());
                loanInfoDto.setStatus(loanList.get(i).getStatus());
                loanInfoDto.setInterestRate(loanList.get(i).getInterestRate());
                loanInfoDto.setPayableLoanAmount(loanList.get(i).getPayableLoanAmount());
                loanInfoDto.setEmail(email);
                loanInfoDto.setMonthlyEMI(loanList.get(i).getMonthlyEMI());
                loanInfoDto.setFine(loanList.get(i).getCurrentFine());
                loanInfoDto.setStartDate(loanList.get(i).getStartDate());
                loanInfoDto.setRePaymentTerm(loanList.get(i).getRePaymentTerm());
            } else
                return new LoanInfoDto();
        }
        return loanInfoDto;
    }

    // From here
    @Override
    public MonthlyEmiDto payEMI(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        MonthlyEmiDto monthlyEmiDto = new MonthlyEmiDto();
        List<Loan> loanList = acc.getLoanList();
        for (int i = 0; i < loanList.size(); i++) {
            if (checkForExistingLoan(email) == Boolean.FALSE) {
                if(penaltyService.noOfMonthsEmiMissed(loanList.get(i).getLoanId())==0) {
                    double payableLoanAmount = loanList.get(i).getPayableLoanAmount();
                    double temp = payableLoanAmount;
                    payableLoanAmount = temp - loanList.get(i).getMonthlyEMI();

                    loanList.get(i).setPayableLoanAmount(payableLoanAmount);
                    loanList.get(i).setEmiDate(firstDateOfNextMonth(LocalDate.now()));

                    LocalDate endDate = ChronoUnit.DAYS.addTo(loanList.get(i).getStartDate(), loanList.get(i).getRePaymentTerm());
                    int rePaymentTermLeft = (int) ChronoUnit.DAYS.between(endDate, LocalDate.now());

                    monthlyEmiDto.setPayableLoanAmount(payableLoanAmount);
                    monthlyEmiDto.setMonthlyEMI(loanList.get(i).getMonthlyEMI());
                    monthlyEmiDto.setRePaymentTermLeft(rePaymentTermLeft);
                    monthlyEmiDto.setPaymentDate(LocalDate.now());
                    monthlyEmiDto.setNextEMIDate(firstDateOfNextMonth(LocalDate.now()));
                }
                else {
                    return payEMIWithFine(email);
                }
            } else
                return new MonthlyEmiDto();
        }
        return monthlyEmiDto;
        // In return - EMI paid, EMI month, Months left, amount left, next payment date
    }

    private MonthlyEmiDto payEMIWithFine(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        MonthlyEmiDto monthlyEmiDto = new MonthlyEmiDto();
        List<Loan> loanList = acc.getLoanList();
        for (int i = 0; i < loanList.size(); i++) {
            if (checkForExistingLoan(email) == Boolean.FALSE) {
                Penalty penalty = penaltyService.chargePenaltyForLoan(loanList.get(i).getLoanId());
                double payableLoanAmount = loanList.get(i).getPayableLoanAmount();
                double temp = payableLoanAmount;
                payableLoanAmount = temp - loanList.get(i).getMonthlyEMI();

                loanList.get(i).setPayableLoanAmount(payableLoanAmount);
                loanList.get(i).setEmiDate(firstDateOfNextMonth(LocalDate.now()));
                penalty.setPenaltyStatus(PenaltyStatus.PAID);
                penaltyRepo.save(penalty);

                LocalDate endDate = ChronoUnit.DAYS.addTo(loanList.get(i).getStartDate(), loanList.get(i).getRePaymentTerm());
                int rePaymentTermLeft = (int) ChronoUnit.DAYS.between(endDate, LocalDate.now());

                monthlyEmiDto.setPayableLoanAmount(payableLoanAmount);
                monthlyEmiDto.setMonthlyEMI(loanList.get(i).getMonthlyEMI()+loanList.get(i).getCurrentFine());// monthly emi is inc by currentFine
                monthlyEmiDto.setRePaymentTermLeft(rePaymentTermLeft);
                monthlyEmiDto.setPaymentDate(LocalDate.now());
                monthlyEmiDto.setNextEMIDate(firstDateOfNextMonth(LocalDate.now()));
                loanList.get(i).setCurrentFine(0);//set currentFine to 0
            }
            else
                return new MonthlyEmiDto();
        }
        return monthlyEmiDto;
        // In return - EMI paid, EMI month, Months left, amount left, next payment date
    }

    @Override
    public LoanClosureDto getLoanClosureDetails(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        LoanClosureDto loanClosureDto = new LoanClosureDto();
        List<Loan> loanList = acc.getLoanList();
        for (int i = 0; i < loanList.size(); i++) {
            if (checkForExistingLoan(email) == Boolean.FALSE) {
                double monthlyEMI = loanList.get(i).getMonthlyEMI();
                loanClosureDto.setStatus(LoanStatus.REQUESTEDFORFORECLOSURE);
                loanClosureDto.setLoanType(loanList.get(i).getLoanType());
                loanClosureDto.setFine(monthlyEMI / 100);
                loanClosureDto.setPrincipalLoanAmount(loanList.get(i).getPrincipalLoanAmount());
                loanClosureDto.setLastEMIDate(firstDateOfNextMonth(LocalDate.now()));
                loanClosureDto.setStartDate(loanList.get(i).getStartDate());
                loanClosureDto.setMonthlyEMI(loanList.get(i).getPayableLoanAmount() + monthlyEMI / 100);
                loanClosureDto.setRePaymentTerm((int) ChronoUnit.DAYS.between(loanList.get(i).getStartDate(), firstDateOfNextMonth(LocalDate.now())));
                loanClosureDto.setFinalStatement("The Loan Closure for your loan");
            }
            else
                return new LoanClosureDto();
        }
        return loanClosureDto;
    }
    public String applyForLoanClosure(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        List<Loan> loanList = acc.getLoanList();
        for (int i = 0; i < loanList.size(); i++) {
            if (checkForExistingLoan(email) == Boolean.FALSE) {
                if (loanList.get(i).getStatus() == LoanStatus.APPROVED || loanList.get(i).getStatus() == LoanStatus.SANCTIONED) {
                    double monthlyEMI = loanList.get(i).getMonthlyEMI();
                    loanList.get(i).setStatus(LoanStatus.REQUESTEDFORFORECLOSURE);
                    loanList.get(i).setCurrentFine(monthlyEMI / 100);
                    loanList.get(i).setMonthlyEMI(loanList.get(i).getPayableLoanAmount() + monthlyEMI / 100);
                    loanList.get(i).setRePaymentTerm((int) ChronoUnit.DAYS.between(loanList.get(i).getStartDate(), firstDateOfNextMonth(LocalDate.now())));
                } else
                    return "Error";
            }
            else
                return "Error";
        }
        return "Applied For Closure";
    }
    public LocalDate firstDateOfNextMonth(LocalDate date) {
        LocalDate nextMonth = date.plusMonths(1);
        return nextMonth.withDayOfMonth(1);
    }
    // to here


    //prince
    @Override
	public List<LoanHIstoryDTO> getLoansByLoanType(String loanType) {
		Loan loan1 = new Loan();
		List<Loan> loans = loanRepository.findAll().stream().filter((loan) -> loan1.getLoanType().equals(loanType))
				.collect(Collectors.toList());
		List<LoanHIstoryDTO> loanDTOList = new ArrayList<>();
		for (Loan loan : loans) {
//			User user = new User();
			LoanHIstoryDTO loanhistortDTO = new LoanHIstoryDTO();

			loanhistortDTO.setLoanId(loan.getLoanId());
			// loanhistortDTO.setLoanType(loanType);

			loanhistortDTO.setRequestedLoanAmount(loan.getPrincipalLoanAmount());
			loanhistortDTO.setInterestRate(loan.getInterestRate());
			loanhistortDTO.setMonthlyEmi(loan.getMonthlyEMI());
			loanhistortDTO.setUserName(loan.getAccount().getUser().getUserName());
			loanDTOList.add(loanhistortDTO);

		}
		return loanDTOList;

	}

	@Override
	public List<LoanHIstoryDTO> getLoansByLoanStatus(String loanStatus) {
		Loan loan1 = new Loan();
		List<Loan> loans = loanRepository.findAll().stream().filter((loan) -> loan1.getStatus().equals(loanStatus))
				.collect(Collectors.toList());
		List<LoanHIstoryDTO> loanDTOList = new ArrayList<>();
		for (Loan loan : loans) {
//			User user = new User();
			LoanHIstoryDTO loanhistortDTO = new LoanHIstoryDTO();

			loanhistortDTO.setLoanId(loan.getLoanId());
			// loanhistortDTO.setLoanType(loanType);

			loanhistortDTO.setRequestedLoanAmount(loan.getPrincipalLoanAmount());
			loanhistortDTO.setInterestRate(loan.getInterestRate());
			loanhistortDTO.setMonthlyEmi(loan.getMonthlyEMI());
			loanhistortDTO.setUserName(loan.getAccount().getUser().getUserName());
			loanDTOList.add(loanhistortDTO);

		}
		return loanDTOList;
	}
}
