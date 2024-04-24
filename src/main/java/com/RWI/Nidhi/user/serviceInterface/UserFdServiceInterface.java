package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.FdDto;
import com.RWI.Nidhi.entity.FixedDeposit;
import org.springframework.http.ResponseEntity;

public interface UserFdServiceInterface {
    FixedDeposit createFd(FdDto fdDto);
    Double closeFd(int fdId);
    Double calculateOnMatureFdAmount(int fdId);
}
