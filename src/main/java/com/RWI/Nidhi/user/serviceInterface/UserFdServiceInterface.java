package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.FdDto;
import com.RWI.Nidhi.dto.FdRequestDto;
import com.RWI.Nidhi.dto.FdResponseDto;

import java.util.List;

public interface UserFdServiceInterface {
    FdResponseDto createFd( String email, FdDto fdDto);

    Double closeFd(int fdId) throws Exception;

    FdRequestDto getFdById(int fdId) throws Exception;

    List<FdRequestDto> getFdByEmail(String email);
}
