package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.FdDto;
import com.RWI.Nidhi.dto.FdResponseDto;
import com.RWI.Nidhi.dto.FdRequestDto;

import java.util.List;


public interface UserFdServiceInterface {
    FdResponseDto createFd(String agentEmail, String email, FdDto fdDto);

    Double closeFd(int fdId);

    FdRequestDto getFdById(int fdId);

    List<FdRequestDto> getFdByEmail(String email);
}
