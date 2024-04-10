package com.Railworld.NidhiBankMonolithic.service;


import com.Railworld.NidhiBankMonolithic.dto.LoanDto;
import com.Railworld.NidhiBankMonolithic.dto.MemberDto;
import com.Railworld.NidhiBankMonolithic.model.Account;
import com.Railworld.NidhiBankMonolithic.model.Application;
import com.Railworld.NidhiBankMonolithic.model.Loan;
import com.Railworld.NidhiBankMonolithic.model.Member;
import com.Railworld.NidhiBankMonolithic.repo.LoanRepository;
import com.Railworld.NidhiBankMonolithic.repo.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MemberService {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    LoanService loanService;
    @Autowired
    AccountService accountService;

    @Autowired
    CompanyService companyService;

    @Autowired
    UserService userService;


    public void payLoan(Long accountNo,Double amount) {
        Loan loan = loanService.getLoan(accountNo);
        Double nowPendings = loan.getAmount() - amount;
        if(nowPendings == 0) loanService.removeLoan(loan.getId());
        else{
            loan.setAmount(nowPendings);
        }
    }

    public void applyLoan(LoanDto loanDto) {
        loanService.applyLoan(loanDto);
    }
    public Integer addMember(MemberDto memberDto) {
        Member member = new Member();
        member.setUser(userService.findByUEmail(memberDto.getEmail()));
        member.setAddress(memberDto.getAddress());
        member.setCompany(companyService.getById(memberDto.getUserDto().getCompanyId()));
        member.setMEmail(memberDto.getEmail());
        member.setMRegistrationDate((new Date()).toString());
        member.setMName(memberDto.getName());
        member.setPhoneNo(memberDto.getPhone());
        Account account = accountService.createAccount(member,memberDto);
        member.setAccount(account);
        memberRepository.save(member);
        return member.getMId();
    }
    public List<Member> getMembers(int id) {
        List<Member> members = memberRepository.findAllByCId(id);
        return members;
    }

    public Member getMemberById(Integer mId) {
       return memberRepository.findById(mId).get();
    }
}
