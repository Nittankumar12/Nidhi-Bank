package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.MisDto;
import com.RWI.Nidhi.dto.MisRequestDto;
import com.RWI.Nidhi.entity.MIS;


public interface UserMisServiceInterface {

    MisRequestDto createMis(String agentEmail, String email,MisDto misDto);

    Double closeMis(int misId) throws Exception;

}
