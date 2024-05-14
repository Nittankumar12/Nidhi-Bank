package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.SchemeApplyDTO;
import com.RWI.Nidhi.dto.SchemeInfoDto;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.Scheme;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.SchemeStatus;
import com.RWI.Nidhi.repository.UserRepo;
import com.RWI.Nidhi.user.serviceInterface.SchemeServiceInterface;
import com.RWI.Nidhi.repository.SchemeRepo;
import com.RWI.Nidhi.user.serviceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class SchemeServiceImplementation implements SchemeServiceInterface {
    @Autowired
    SchemeRepo schemeRepo;
    @Autowired
    UserService userService;
    @Autowired
    UserRepo userRepo;
    @Autowired
    UserSchemeLoanServiceImplementation userSchemeLoanService;

    @Override
    public int findSchemeRemainingDays(int schemeId) {
        LocalDate startDate = schemeRepo.findStartDateBySchemeId(schemeId);
        int tenure = schemeRepo.findTenureBySchemeId(schemeId);
        LocalDate endDate = startDate.plus(tenure, ChronoUnit.DAYS);
        long temp = ChronoUnit.DAYS.between(endDate, LocalDate.now());
        int remainingDays = (int) temp;
        return remainingDays;
    }

    @Override
    public double findLoanOnSchemeBasis(int schemeId) {
        double schemeLoan = (double) (schemeRepo.findMonthlyDepositAmountBySchemeId(schemeId) / 30 * findSchemeRemainingDays(schemeId));
        return schemeLoan;
    }
    public boolean CheckForSchemeRunning(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        Scheme scheme = acc.getScheme();
        if (scheme != null) {
            return Boolean.FALSE;
        } else
            return Boolean.TRUE;
    }
    @Override
    public ResponseEntity<?> addScheme(SchemeApplyDTO schemeApplyDTO) {
        if (userRepo.existsByEmail(schemeApplyDTO.getEmail())) {
            User user = userService.getByEmail(schemeApplyDTO.getEmail());
            Accounts accounts = user.getAccounts();
            if (accounts != null) {
                Scheme scheme = new Scheme();
                scheme.setTenure(schemeApplyDTO.getTenure());
                scheme.setMonthlyDepositAmount(schemeApplyDTO.getSchemeAmount() / schemeApplyDTO.getTenure());
                scheme.setSStatus(SchemeStatus.APPLIED);
                scheme.setAccount(accounts);
                schemeRepo.save(scheme);
                return new ResponseEntity<>(scheme, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Account not created", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("User doesn't exist", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getSchemeInfo(String email) {
        SchemeInfoDto schemeInfoDto = new SchemeInfoDto();
        if (userRepo.existsByEmail(email)) {
            User user = userService.getByEmail(email);
            Accounts accounts = user.getAccounts();
            Scheme scheme = accounts.getScheme();
            if(scheme!=null) {
                schemeInfoDto.setMonthlyDepositAmount(scheme.getMonthlyDepositAmount());
                schemeInfoDto.setStartDate(scheme.getStartDate());
                schemeInfoDto.setNextEMIDate(scheme.getNextEMIDate());
                schemeInfoDto.setTenure(scheme.getTenure());
                schemeInfoDto.setTotalDepositAmount(scheme.getTotalDepositAmount());
                schemeInfoDto.setInterestRate(scheme.getInterestRate());
                schemeInfoDto.setSStatus(scheme.getSStatus());
                schemeInfoDto.setAccounts(accounts);
                schemeInfoDto.setUser(user);
                return new ResponseEntity<>(schemeInfoDto, HttpStatus.FOUND);
            }else {
                return new ResponseEntity<>("Scheme not found",HttpStatus.NOT_FOUND);
            }
        }else
            return new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<?> monthlyDeposit(String email) {
        if (userRepo.existsByEmail(email)) {
            User user = userService.getByEmail(email);
            Accounts accounts = user.getAccounts();
            Scheme scheme = accounts.getScheme();
            if (scheme.getSStatus() == SchemeStatus.SANCTIONED) {
                double total = scheme.getTotalDepositAmount();
                double monthly = scheme.getMonthlyDepositAmount();
                double rate = scheme.getInterestRate();
                double newTotal = total + monthly + (monthly*rate/100);
                scheme.setTotalDepositAmount(newTotal);
                scheme.setNextEMIDate(firstDateOfNextMonth(LocalDate.now()));
                schemeRepo.save(scheme);
                return new ResponseEntity<>("Monthly Deposit successfull", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("No Current Sanctioned Scheme", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Invalid User", HttpStatus.I_AM_A_TEAPOT);
        }
    }
    @Override
    public ResponseEntity<?> getSchemeLoanInfo(String email) {
        return userSchemeLoanService.getLoanInfo(email);
    }
    @Override
    public ResponseEntity<?> applyForSchemeLoan(String email) {
        return userSchemeLoanService.applySchemeLoan(email);
    }
    private LocalDate firstDateOfNextMonth(LocalDate date) {
        LocalDate nextMonth = date.plusMonths(1);
        return nextMonth.withDayOfMonth(1);
    }
}