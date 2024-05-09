package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.MisDto;
import com.RWI.Nidhi.dto.MisRequestDto;

import java.util.List;


public interface UserMisServiceInterface {

    MisRequestDto createMis(String agentEmail, String email, MisDto misDto);

    Double closeMis(int misId) throws Exception;

    List<MisDto> getMisByEmail(String email);

    MisDto getMisById(int misId);

}
