package com.nidhi.kyc.KYC.Service;

import com.nidhi.kyc.KYC.Dto.KycDetailsDto;
import com.nidhi.kyc.KYC.Dto.ResponseKycDto;
import com.nidhi.kyc.KYC.Entity.KycDetails;
import com.nidhi.kyc.KYC.Repo.KycDetailsRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KycDetailsServiceImp implements KycDetailsService {

    @Autowired
    private KycDetailsRepo kycDetailsRepo;


    @Override
    public KycDetailsDto saveKycDetails(KycDetailsDto kycDetailsDTO) {

        KycDetails kycDetails = convertToEntity(kycDetailsDTO);
        kycDetailsRepo.save(kycDetails);
        return kycDetailsDTO;
    }

    private KycDetails convertToEntity(KycDetailsDto kycDto) {
        KycDetails kycEntity = new KycDetails();
        kycEntity.setFirstName(kycDto.getFirstName());
        kycEntity.setLastName(kycDto.getLastName());
        kycEntity.setFatherName(kycDto.getFatherName());
        kycEntity.setFatherLastName(kycDto.getFatherLastName());
        kycEntity.setEmail(kycDto.getEmail());
        kycEntity.setPhnNo(kycDto.getPhnNo());
        kycEntity.setEducation(kycDto.getEducation());
        kycEntity.setGender(kycDto.getGender());
        kycEntity.setDateOfBirth(kycDto.getDateOfBirth());
        kycEntity.setReligion(kycDto.getReligion());
        kycEntity.setNationalityType(kycDto.getNationalityType());
        kycEntity.setCategories(kycDto.getCategories());
        kycEntity.setOccupation(kycDto.getOccupation());
        kycEntity.setAlternatePhnNo(kycDto.getAlternatePhnNo());
        kycEntity.setNomineeFirstName(kycDto.getNomineeFirstName());
        kycEntity.setNomineeLastName(kycDto.getNomineeLastName());
        kycEntity.setNomineeContactNumber(kycDto.getNomineeContactNumber());
        kycEntity.setRelationWithNominee(kycDto.getRelationWithNominee());
        kycEntity.setOccupation(kycDto.getOccupation());
        kycEntity.setMonthlyIncome(kycDto.getMonthlyIncome());
        kycEntity.setNumberOfFamilyMembers(kycDto.getNumberOfFamilyMembers());
        return kycEntity;
    }

    public ResponseKycDto getSomeDetails(Long kycId){
        KycDetails kycDetails = kycDetailsRepo.
                findById(kycId).orElseThrow(()-> new RuntimeException("Id not found"));
        ResponseKycDto responseKycDto = new ResponseKycDto();
        responseKycDto.setEducation(kycDetails.getEducation());
        responseKycDto.setGender(kycDetails.getGender());
        responseKycDto.setEmail(kycDetails.getEmail());
        responseKycDto.setFirstName(kycDetails.getFirstName());
        responseKycDto.setLastName(kycDetails.getLastName());
        responseKycDto.setPhnNo(kycDetails.getPhnNo());
        responseKycDto.setAlternatePhnNo(kycDetails.getAlternatePhnNo());
        responseKycDto.setDateOfBirth(kycDetails.getDateOfBirth());
        return responseKycDto;
    }

    @Override
    public List<KycDetails> getAll() {

        return kycDetailsRepo.findAll();
    }

    @Override
    public KycDetails getDetailsById(Long kycId) {
        return kycDetailsRepo.findById(kycId).orElseThrow(() -> new RuntimeException("Id not found:"));

    }


}