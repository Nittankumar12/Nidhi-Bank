package com.Railworld.NidhiBank.service;

import com.Railworld.NidhiBank.dto.CompanyRequestDto;
import com.Railworld.NidhiBank.model.Company;
import com.Railworld.NidhiBank.repo.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;
    public Company registerCompany(CompanyRequestDto companyRequestDto){
        Company company = new Company();
        company.setCAddress(companyRequestDto.getCompanyAddress());
//        System.out.println(companyRequestDto.getCAddress());
        company.setCBalance(companyRequestDto.getCompanyBalance());
        company.setCName(companyRequestDto.getCompanyName());
        company.setCEmail(companyRequestDto.getCompanyEmail());
//        company.setUserList(companyRequestDto.getUserList());
//        company.setMemberList(companyRequestDto.getMemberList());
        return companyRepository.save(company);
    }
    public Company getCompany(int id) {
        Optional<Company> company = companyRepository.findById(id);
        return company.get();
    }


}
