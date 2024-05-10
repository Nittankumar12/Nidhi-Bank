package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.FdRequestDto;
import com.RWI.Nidhi.dto.MisRequestDto;
import com.RWI.Nidhi.dto.RdRequestDto;
import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.entity.Scheme;
import com.RWI.Nidhi.entity.Transactions;

import java.util.List;

public interface UserStatementService {
    List<FdRequestDto> getFixedDepositDetailsByEmail(String email);

    List<RdRequestDto> getRecurringDepositDetailsByEmail(String email);

    List<MisRequestDto> getMisDetailsByEmail(String email);

    Scheme getSchemeDetailsByEmail(String email);

    List<Transactions> getTransactionsDetailsByEmail(String email);

    List<Loan> getLoanDetailsByEmail(String email);
}

