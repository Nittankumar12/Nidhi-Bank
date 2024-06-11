package com.RWI.Nidhi.user.serviceInterface;

//import com.nidhi.kyc.KYC.Dto.KycDetailsDto;

import com.RWI.Nidhi.dto.KycDetailsDto;
import com.RWI.Nidhi.dto.ResponseKycDto;
import com.RWI.Nidhi.entity.KycDetails;
import com.RWI.Nidhi.enums.KycStatus;

import java.util.List;


public interface KycDetailsService {

    KycDetailsDto saveKycDetails(KycDetailsDto kycDetailsDTO);

    KycStatus findKycByEmail(String email);

    ResponseKycDto getSomeDetails(Long kycId);

    List<KycDetails> getAll();

    KycDetails getDetailsByUserEmail(String userEmail);
}
