package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.FdRequestDto;
import com.RWI.Nidhi.dto.MisRequestDto;
import com.RWI.Nidhi.dto.RdRequestDto;
import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.repository.*;
import com.RWI.Nidhi.user.serviceInterface.UserStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserStatementServiceImpl implements UserStatementService {

    @Autowired
    private LoanRepo loanRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SchemeRepo schemeRepo;
    @Autowired
    private FixedDepositRepo fdRepo;
    @Autowired
    private RecurringDepositRepo rdRepo;
    @Autowired
    private MisRepo misRepo;


    @Override
    public List<FdRequestDto> getFixedDepositDetailsByEmail(String email) {
//        User user = userRepo.findById(userId).orElseThrow(()->new EntityNotFoundException("User not found with ID: " + userId));
        User user = userRepo.findByEmail(email);
        List<FixedDeposit> fixedDepositList = user.getAccounts().getFdList();
        List<FdRequestDto> fdRequestDtoList = new ArrayList<FdRequestDto>();
        for (FixedDeposit fixedDeposit : fixedDepositList) {
            FdRequestDto tempFdResponse = new FdRequestDto();
            tempFdResponse.setUserName(fixedDeposit.getAccount().getUser().getUserName());
            tempFdResponse.setFdId(fixedDeposit.getFdId());
            tempFdResponse.setAmount(fixedDeposit.getAmount());
            tempFdResponse.setFdStatus(fixedDeposit.getFdStatus());

            fdRequestDtoList.add(tempFdResponse);
        }
        System.out.println("your size is " + fdRequestDtoList.size());
        return fdRequestDtoList;
    }

    @Override
    public List<RdRequestDto> getRecurringDepositDetailsByEmail(String email) {
        User user = userRepo.findByEmail(email);
        List<RecurringDeposit> recurringDepositList = user.getAccounts().getRecurringDepositList();
        List<RdRequestDto> rdRequestDtoList = new ArrayList<RdRequestDto>();
        for (RecurringDeposit recurringDeposit : recurringDepositList) {
            RdRequestDto tempRdResponse = new RdRequestDto();
            tempRdResponse.setUserName(recurringDeposit.getAccount().getUser().getUserName());
            tempRdResponse.setRdId(recurringDeposit.getRdId());
            tempRdResponse.setMonthlyDepositAmount(recurringDeposit.getMonthlyDepositAmount());
            tempRdResponse.setRdStatus(recurringDeposit.getRdStatus());
            rdRequestDtoList.add(tempRdResponse);
        }
        return rdRequestDtoList;
    }

    @Override
    public List<MisRequestDto> getMisDetailsByEmail(String email) {
        User user = userRepo.findByEmail(email);
        List<MIS> misList = user.getAccounts().getMisList();
        List<MisRequestDto> misRequestDtoList = new ArrayList<MisRequestDto>();
        for (MIS mis : misList) {
            MisRequestDto tempMisResponse = new MisRequestDto();
            tempMisResponse.setUserName(mis.getAccount().getUser().getUserName());
            tempMisResponse.setMisId(mis.getMisId());
            tempMisResponse.setTotalDepositedAmount(mis.getTotalDepositedAmount());
            tempMisResponse.setStatus(mis.getStatus());
            misRequestDtoList.add(tempMisResponse);
        }
        return misRequestDtoList;
    }

    @Override
    public Scheme getSchemeDetailsByEmail(String email) {
        User user = userRepo.findByEmail(email);
        return user.getAccounts().getScheme();
    }

    @Override
    public List<Transactions> getTransactionsDetailsByEmail(String email) {
        User user = userRepo.findByEmail(email);
        return user.getAccounts().getTransactionsList();
    }

    @Override
    public List<Loan> getLoanDetailsByEmail(String email) {
        User user = userRepo.findByEmail(email);
        return user.getAccounts().getLoanList();
    }

}
