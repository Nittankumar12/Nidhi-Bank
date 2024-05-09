package com.nidhi.kyc.KYC.Service;

import com.nidhi.kyc.KYC.Dto.KycDetailsDto;
import com.nidhi.kyc.KYC.Dto.ResponseKycDto;
import com.nidhi.kyc.KYC.Entity.KycDetails;
import com.nidhi.kyc.KYC.Repo.KycDetailsRepo;
import org.springframework.stereotype.Service;

import java.util.List;


public interface KycDetailsService {

    KycDetailsDto saveKycDetails(KycDetailsDto kycDetailsDTO);

    ResponseKycDto getSomeDetails(Long kycId);

    List<KycDetails> getAll();

    KycDetails getDetailsById(Long kycId);
}
