package com.RWI.Nidhi.user.serviceInterface;


public interface SchemeService {
    int findSchemeRemainingDays(int schemeId);
    double findLoanOnSchemeBasis(int schemeId);
}
