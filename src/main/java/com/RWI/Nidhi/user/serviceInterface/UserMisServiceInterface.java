package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.entity.MIS;


public interface UserMisServiceInterface {
    MIS createMis(MIS rd);
    Double closeMis(int misId);

    Double onMaturity(Double amount, Integer tenure, Double interestRate);
}
