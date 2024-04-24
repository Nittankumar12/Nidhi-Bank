package com.RWI.Nidhi.user.repository;
import com.RWI.Nidhi.entity.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;

public interface SchemeRepo extends JpaRepository <Scheme, Integer>{
    LocalDate findStartDateBySchemeId(int schemeId);
    int findTenureBySchemeId(int schemeId);

    int findMonthlyDepositAmountBySchemeID(int schemeId);
}
