package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.FdResponseDto;
import com.RWI.Nidhi.dto.MisResponseDto;
import com.RWI.Nidhi.dto.RdResponseDto;
import com.RWI.Nidhi.entity.*;
import java.util.List;

public interface UserDetailsService {
    List<FdResponseDto> getFixedDepositDetailsByUserId(int userId);
    List<RdResponseDto> getRecurringDepositDetailsByUserId(int userId);
    List<MisResponseDto> getMisDetailsByUserId(int userId);
    Scheme getSchemeByDetailsUserId(int userId);
    List<Transactions> getTransactionsDetailsByUserId(int userId);
    List<Loan> getLoanDetailsByUserId(int userId);
}
