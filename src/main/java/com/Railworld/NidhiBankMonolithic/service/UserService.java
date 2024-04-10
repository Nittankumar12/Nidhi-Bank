package com.Railworld.NidhiBankMonolithic.service;

import com.Railworld.NidhiBankMonolithic.dto.MemberDto;
import com.Railworld.NidhiBankMonolithic.dto.UserDto;
import com.Railworld.NidhiBankMonolithic.model.Account;
import com.Railworld.NidhiBankMonolithic.model.Member;
import com.Railworld.NidhiBankMonolithic.model.User;
import com.Railworld.NidhiBankMonolithic.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CompanyService companyService;
    @Autowired
    MemberService memberService;

    public String register(UserDto userDto) {
        User user = new User();
        user.setJoined(false);
        user.setUEmail(userDto.getEmail());
        user.setUPassword(userDto.getPassword());
        user.setCompany(companyService.getById(userDto.getCompanyId()));
        userRepository.save(user);
        return "registered";
    }
    public User getUser(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.get();
    }
    public Integer joinBank(MemberDto memberDto) {
        Integer id = memberService.addMember(memberDto);
        System.out.println("Member added to the memberRepo");
        return id;
    }

    public User findByUEmail(String email) {
        return  userRepository.findByuEmail(email);
    }

    public void updateMember(Integer mId,Integer uId) {
        User user = userRepository.findById(uId).get();
        user.setMember(memberService.getMemberById(mId));
//        user.setMember(memberService.getMemberById(mId));
        user.setJoined(true);
        userRepository.save(user);
    }
}
