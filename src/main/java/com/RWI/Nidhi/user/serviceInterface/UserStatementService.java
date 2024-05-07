package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.FdResponseDto;
import com.RWI.Nidhi.dto.MisResponseDto;
import com.RWI.Nidhi.dto.RdResponseDto;
import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.entity.Scheme;
import com.RWI.Nidhi.entity.Transactions;

import java.util.List;

public interface UserStatementService {
    List<FdResponseDto> getFixedDepositDetailsByEmail(String email);

    List<RdResponseDto> getRecurringDepositDetailsByEmail(String email);

    List<MisResponseDto> getMisDetailsByEmail(String email);

    Scheme getSchemeDetailsByEmail(String email);

    List<Transactions> getTransactionsDetailsByEmail(String email);

    List<Loan> getLoanDetailsByEmail(String email);
}

