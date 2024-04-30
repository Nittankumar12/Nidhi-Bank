package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.FdDto;
import com.RWI.Nidhi.entity.FixedDeposit;
import java.util.List;
import java.util.Optional;


public interface UserFdServiceInterface {
    FixedDeposit createFd(FdDto fdDto);
    Double closeFd(int fdId);
    List<FixedDeposit> getAllFds();
    Optional<FixedDeposit> getFdById(int fdId);
}
