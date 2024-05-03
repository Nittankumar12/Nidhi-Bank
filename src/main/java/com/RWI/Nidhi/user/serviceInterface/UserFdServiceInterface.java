package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.FdDto;
import com.RWI.Nidhi.entity.FixedDeposit;
import java.util.List;
import java.util.Optional;

import java.util.List;


public interface UserFdServiceInterface {
    FixedDeposit createFd(FdDto fdDto);
    Double closeFd(int fdId);
    List<FixedDeposit> getAllFds();
    FixedDeposit getFdById(int fdId);
}
