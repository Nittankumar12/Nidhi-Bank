package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.*;
import com.RWI.Nidhi.entity.Scheme;
import com.RWI.Nidhi.entity.Transactions;

import java.util.List;

public interface UserStatementService {
    List<FdRequestDto> getFixedDepositDetailsByEmail(String email);

    List<RdRequestDto> getRecurringDepositDetailsByEmail(String email);

    List<MisRequestDto> getMisDetailsByEmail(String email);

    List<SchemeDto> getSchemeDetailsByEmail(String email);

    List<Transactions> getTransactionsDetailsByEmail(String email);

    List<LoanHistoryDTO> getLoanDetailsByEmail(String email);
}

