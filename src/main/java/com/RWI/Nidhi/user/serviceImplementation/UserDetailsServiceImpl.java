package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.repository.*;
import com.RWI.Nidhi.user.serviceInterface.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

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
    public Loan getLoanDetailsByUserId(int userId) throws UserPrincipalNotFoundException {
         User user = userRepo.findById(userId).orElseThrow(()->new UserPrincipalNotFoundException("User not found :" + userId + ""));
         return user.getAccounts().getLoan();
    }

    @Override
    public List<Transactions> getTransactionsDetailsByUserId(int userId) throws UserPrincipalNotFoundException {
        User user = userRepo.findById(userId).orElseThrow(()->new UserPrincipalNotFoundException("User not found :" + userId + ""));
        return user.getAccounts().getTransactionsList();
    }

    @Override
    public List<FixedDeposit> getFixedDepositDetailsByUserId(int userId) throws UserPrincipalNotFoundException {
        User user = userRepo.findById(userId).orElseThrow(()->new UserPrincipalNotFoundException("User not found :" + userId + ""));
        return user.getAccounts().getFdList();
    }

    @Override
    public List<RecurringDeposit> getRecurringDepositDetailsByUserId(int userId) throws UserPrincipalNotFoundException {
        User user = userRepo.findById(userId).orElseThrow(()->new UserPrincipalNotFoundException("User not found :" + userId + ""));

        return user.getAccounts().getRecurringDepositList();
    }

    @Override
    public List<MIS> getMisByDetailsUserId(int userId) throws UserPrincipalNotFoundException {
        User user = userRepo.findById(userId).orElseThrow(()->new UserPrincipalNotFoundException("User not found :" + userId + ""));

        return user.getAccounts().getMisList();
    }

    @Override
    public List<Scheme> getSchemeByDetailsUserId(int userId) throws UserPrincipalNotFoundException {
        User user = userRepo.findById(userId).orElseThrow(()->new UserPrincipalNotFoundException("User not found :" + userId + ""));

        return user.getAccounts().getSchemeList();
    }
//    @Autowired
//    private TransactionsRepo transactionsRepo;

}
