package com.RWI.Nidhi.user.serviceInterface;


public interface SchemeServiceInterface {
    int findSchemeRemainingDays(int schemeId);

    double findLoanOnSchemeBasis(int schemeId);
}
