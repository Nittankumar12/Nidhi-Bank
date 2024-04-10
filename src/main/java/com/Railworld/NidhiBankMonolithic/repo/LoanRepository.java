package com.Railworld.NidhiBankMonolithic.repo;

import com.Railworld.NidhiBankMonolithic.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Integer> {

}
