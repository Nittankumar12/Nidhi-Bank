package com.Railworld.NidhiBank.service;

import com.Railworld.NidhiBank.dto.UserRequestDto;
import com.Railworld.NidhiBank.model.Company;
import com.Railworld.NidhiBank.model.User;
import com.Railworld.NidhiBank.repo.CompanyRepository;
import com.Railworld.NidhiBank.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;

    public void registerUser(UserRequestDto userRequestDto){
        User user = new User();
        Optional<Company> company = companyRepository.findById(userRequestDto.getCompanyId());
        user.setCompany(company.get());
        user.setUEmail(userRequestDto.getUserEmail());
        user.setUPassword(userRequestDto.getUserPassword());
        user.setUName(userRequestDto.getUserName());
        userRepository.save(user);
    }

    public User getUser(int id){
        Optional<User> user = userRepository.findById(id);
//        System.out.println(user.get().getUPassword());
        return user.get();
    }
}
