package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.Scheme;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;

@Repository
public interface SchemeRepo extends JpaRepository<Scheme, Integer> {
	@Transactional
	@Modifying
	@Query(value = "SELECT s.start_Date FROM Scheme s WHERE s.scheme_id = :schemeId", nativeQuery = true)
	LocalDate findStartDateBySchemeId(@Param("schemeId") int schemeId);
	@Transactional
	@Modifying
	@Query(value = "SELECT s.tenure FROM Scheme s WHERE s.scheme_id = :schemeId",nativeQuery = true)
	int findTenureBySchemeId(@Param("schemeId") int schemeId);
	@Transactional
	@Modifying
	@Query(value = "SELECT s.monthly_deposit_amount FROM Scheme s WHERE s.scheme_id = :schemeId",nativeQuery = true)
	int findMonthlyDepositAmountBySchemeId(int schemeId);
}
