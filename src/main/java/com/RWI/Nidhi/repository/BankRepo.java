package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.BankDetails;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepo extends JpaRepository<BankDetails,Integer> {
}
