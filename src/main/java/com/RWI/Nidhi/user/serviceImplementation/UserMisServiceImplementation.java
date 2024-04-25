package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.entity.MIS;
import com.RWI.Nidhi.user.repository.MisRepo;
import com.RWI.Nidhi.user.serviceInterface.UserMisServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;

public class UserMisServiceImplementation implements UserMisServiceInterface {
    @Autowired
    private MisRepo misRepo;

    @Override
    public MIS createMis(MIS rd) {
        return null;
    }

    @Override
    public Double closeMis(int misId) {
        return null;
    }

    @Override
    public Double onMaturity(Double amount, Integer tenure, Double interestRate) {
        return null;
    }
}
