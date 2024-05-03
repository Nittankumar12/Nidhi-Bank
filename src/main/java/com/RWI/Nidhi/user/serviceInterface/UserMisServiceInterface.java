package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.MisDto;
import com.RWI.Nidhi.entity.MIS;


public interface UserMisServiceInterface {

    MIS createMis(MisDto misDto);
    Double closeMis(int misId) throws Exception;

}
