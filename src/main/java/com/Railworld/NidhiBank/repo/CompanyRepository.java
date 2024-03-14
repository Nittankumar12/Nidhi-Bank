package com.Railworld.NidhiBank.repo;

import com.Railworld.NidhiBank.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Integer> {

}
