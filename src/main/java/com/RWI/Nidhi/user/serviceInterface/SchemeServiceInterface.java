package com.RWI.Nidhi.user.serviceInterface;


import com.RWI.Nidhi.dto.SchemeApplyDTO;

public interface SchemeServiceInterface {
    int findSchemeRemainingDays(int schemeId);

    double findLoanOnSchemeBasis(int schemeId);
    void addScheme(SchemeApplyDTO schemeApplyDTO);
}
