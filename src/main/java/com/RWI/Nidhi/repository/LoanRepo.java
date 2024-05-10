package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.Security.models.Credentials;
import com.RWI.Nidhi.entity.Agent;
import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.enums.LoanStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepo extends JpaRepository<Loan, Integer> {
    @Transactional
    @Modifying
    @Query("SELECT l.status FROM loan l WHERE l.loan_Id = :loanId")
    LoanStatus findStatusByLoanId(int loanId);
    @Transactional
    @Modifying
    @Query("SELECT l.monthlyemi FROM loan l WHERE l.loan_Id = :loanId")
    double findEMIByLoanId(int loanId);
    @Transactional
    @Modifying
    @Query("SELECT l.current_fine FROM loan l WHERE l.loan_Id = :loanId")
    double findCurrentFineByLoanId(int loanId);
    // neeed to make the query
    List<Loan> findByEmiDateBetween(LocalDate startDate, LocalDate endDate);
    @Transactional
    @Modifying
    @Query("SELECT * FROM loan l WHERE l.status = :status")
    List<Loan> findByStatus(LoanStatus status);
    Agent save(Credentials cred);
}
