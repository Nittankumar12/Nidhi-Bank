package com.Railworld.NidhiBankMonolithic.service;

import com.Railworld.NidhiBankMonolithic.dto.LoanDto;
import com.Railworld.NidhiBankMonolithic.dto.MemberDto;
import com.Railworld.NidhiBankMonolithic.model.Account;
import com.Railworld.NidhiBankMonolithic.model.Loan;
import com.Railworld.NidhiBankMonolithic.model.LoanStatus;
import com.Railworld.NidhiBankMonolithic.model.Member;
import com.Railworld.NidhiBankMonolithic.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    public Loan getLoan(Long accountNo) {
        return accountRepository.findByAccountNo(accountNo).getLoan();
    }

    public Account getAccountById(Integer accountId) {
        return accountRepository.findById(accountId).get();
    }

    public Account createAccount(Member member,MemberDto memberDto) {
        Account account = new Account();
        account.setAccountType(memberDto.getAccountType());
        account.setApplications(new ArrayList<>());
        account.setBalance(memberDto.getBalance());

        account.setBranch(memberDto.getBranch());
        account.setIFSC_code(generateIFSC(memberDto.getBranch()));
        account.setAccountNo(generateAccountNumber());
        account.setPin(generatePin());

        account.setTransactionsFrom(new ArrayList<>());
        account.setTransactionsTo(new ArrayList<>());
        account.setMember(member);
        return accountRepository.save(account);
    }
     public static Long generateAccountNumber() {
       Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            accountNumber.append(random.nextInt(10));
        }
        accountNumber.insert(0, 4);
        return Long.parseLong(accountNumber.toString());
    }

    public static int generatePin() {
        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }

    public static String generateIFSC(String branchName) {
        return branchName.substring(0, 4).toUpperCase() + "0" + (new Random().nextInt(90) + 10);
    }

    public void disburseAmount(Double amount, Account account) {

    }
}
