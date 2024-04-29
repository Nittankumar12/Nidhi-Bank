package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;

@Repository
public interface SchemeRepo extends JpaRepository<Scheme, Integer> {
	LocalDate findStartDateBySchemeId(int schemeId);

	int findTenureBySchemeId(int schemeId);

	int findMonthlyDepositAmountBySchemeId(int schemeId);
}
