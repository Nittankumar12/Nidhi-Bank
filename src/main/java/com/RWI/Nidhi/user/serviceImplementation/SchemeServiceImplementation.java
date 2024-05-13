package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.SchemeApplyDTO;
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
    @Override
    public int findSchemeRemainingDays(int schemeId) {
        LocalDate startDate = schemeRepo.findStartDateBySchemeId(schemeId);
        int tenure = schemeRepo.findTenureBySchemeId(schemeId);
        LocalDate endDate = startDate.plus(tenure, ChronoUnit.DAYS);
        long temp = ChronoUnit.DAYS.between(endDate,LocalDate.now() );
        int remainingDays = (int)temp;
        return remainingDays;
    }

    @Override
    public double findLoanOnSchemeBasis(int schemeId) {
        double schemeLoan = schemeRepo.findMonthlyDepositAmountBySchemeId(schemeId) / 30 * findSchemeRemainingDays(schemeId);
        return schemeLoan;
    }

    public boolean CheckForSchemeRunning(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        Scheme scheme = acc.getScheme();
//        if (scheme.getSchemeId() ) {
//            return Boolean.FALSE;
//        } else
            return Boolean.TRUE;
        // This method is suppossed to return false if there is a scheme running else true
    }
    @Override
    public ResponseEntity<?> addScheme(SchemeApplyDTO schemeApplyDTO) {
        if (userRepo.existsByEmail(schemeApplyDTO.getEmail())) {
            User user = userService.getByEmail(schemeApplyDTO.getEmail());
            Accounts accounts = user.getAccounts();
            Scheme scheme = new Scheme();
            scheme.setTenure(schemeApplyDTO.getTenure());
            scheme.setTotalDepositAmount(schemeApplyDTO.getSchemeAmount());
            scheme.setSStatus(SchemeStatus.APPLIED);
            scheme.setAccount(accounts);
            schemeRepo.save(scheme);
            return new ResponseEntity<>(scheme, HttpStatus.ACCEPTED);
        }
        else
            return new ResponseEntity<>("User doesn't exist",HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> depositMonthlySchPay(String email) {

        return null;
    }
}
