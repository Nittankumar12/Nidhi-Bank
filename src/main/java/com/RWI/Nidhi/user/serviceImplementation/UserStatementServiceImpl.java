package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.FdResponseDto;
import com.RWI.Nidhi.dto.MisResponseDto;
import com.RWI.Nidhi.dto.RdResponseDto;
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
    public List<FdResponseDto> getFixedDepositDetailsByEmail(String email) {
//        User user = userRepo.findById(userId).orElseThrow(()->new EntityNotFoundException("User not found with ID: " + userId));
        User user = userRepo.findByEmail(email);
        List<FixedDeposit> fixedDepositList = user.getAccounts().getFdList();
        List<FdResponseDto> fdResponseDtoList = new ArrayList<FdResponseDto>();
        for (FixedDeposit fixedDeposit : fixedDepositList) {
            FdResponseDto tempFdResponse = new FdResponseDto();
            tempFdResponse.setFdId(fixedDeposit.getFdId());
            tempFdResponse.setAmount(fixedDeposit.getAmount());
            tempFdResponse.setFdStatus(fixedDeposit.getFdStatus());

            fdResponseDtoList.add(tempFdResponse);
        }
//        System.out.println("your size is " + fdResponseDtoList.size());
        return fdResponseDtoList;
    }

    @Override
    public List<RdResponseDto> getRecurringDepositDetailsByEmail(String email) {
        User user = userRepo.findByEmail(email);
        List<RecurringDeposit> recurringDepositList = user.getAccounts().getRecurringDepositList();
        List<RdResponseDto> rdResponseDtoList = new ArrayList<RdResponseDto>();
        for (RecurringDeposit recurringDeposit : recurringDepositList) {
            RdResponseDto tempRdResponse = new RdResponseDto();
            tempRdResponse.setRdId(recurringDeposit.getRdId());
            tempRdResponse.setMonthlyDepositAmount(recurringDeposit.getMonthlyDepositAmount());
            tempRdResponse.setRdStatus(recurringDeposit.getRdStatus());
            rdResponseDtoList.add(tempRdResponse);
        }
        return rdResponseDtoList;
    }

    @Override
    public List<MisResponseDto> getMisDetailsByEmail(String email) {
        User user = userRepo.findByEmail(email);
        List<MIS> misList = user.getAccounts().getMisList();
        List<MisResponseDto> misResponseDtoList = new ArrayList<MisResponseDto>();
        for (MIS mis : misList) {
            MisResponseDto tempMisResponse = new MisResponseDto();
            tempMisResponse.setMisId(mis.getMisId());
            tempMisResponse.setTotalDepositedAmount(mis.getTotalDepositedAmount());
            tempMisResponse.setStatus(mis.getStatus());
            misResponseDtoList.add(tempMisResponse);
        }
        return misResponseDtoList;
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
