package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.entity.Scheme;
import com.RWI.Nidhi.user.repository.AccountsRepo;
import com.RWI.Nidhi.user.serviceInterface.AccountsService;
import com.RWI.Nidhi.user.serviceInterface.SchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

public class AccountsServiceImplementation implements AccountsService {
    @Autowired
    AccountsRepo accountsRepo;
    @Autowired
    SchemeService schemeService;
    @Override
    public Boolean schemeRunning(int accountId) {
        List<Scheme> currentScheme = accountsRepo.findSchemeListByAccountId(accountId);
        for(int i=0; i<currentScheme.size();i++) {
            Scheme sc = currentScheme.get(i);
            int remainingDays = schemeService.findSchemeRemainingDays(sc.getSchemeId());
            if (remainingDays != 0)
                return Boolean.TRUE;
        }
           return Boolean.FALSE;
    }
}
