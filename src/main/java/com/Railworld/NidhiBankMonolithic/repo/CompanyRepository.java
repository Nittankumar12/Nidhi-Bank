package com.Railworld.NidhiBankMonolithic.repo;

import com.Railworld.NidhiBankMonolithic.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CompanyRepository extends JpaRepository<Company,Integer> {

}
