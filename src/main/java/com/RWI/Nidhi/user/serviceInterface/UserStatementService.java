package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.*;
import com.RWI.Nidhi.entity.Transactions;

import java.util.List;

public interface UserStatementService {
    List<FdRequestDto> getFixedDepositDetailsByEmail(String email);

    List<RdRequestDto> getRecurringDepositDetailsByEmail(String email);

    List<MisRequestDto> getMisDetailsByEmail(String email);

    SchemeDto getSchemeDetailsByEmail(String email);

    List<Transactions> getTransactionsDetailsByEmail(String email);

    List<LoanHistoryDto> getLoanDetailsByEmail(String email);
}

