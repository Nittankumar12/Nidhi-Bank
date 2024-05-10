package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.MisDto;
import com.RWI.Nidhi.dto.MisResponseDto;
import com.RWI.Nidhi.dto.MisRequestDto;

import java.util.List;


public interface UserMisServiceInterface {

    MisResponseDto createMis(String agentEmail, String email, MisDto misDto);

    Double closeMis(int misId) throws Exception;

    List<MisRequestDto> getMisByEmail(String email);

    MisRequestDto getMisById(int misId);

}
