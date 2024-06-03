package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.KycDetailsDto;
import com.RWI.Nidhi.dto.ResponseKycDto;
import com.RWI.Nidhi.entity.KycDetails;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.KycStatus;
import com.RWI.Nidhi.repository.KycDetailsRepo;
import com.RWI.Nidhi.repository.UserRepo;
import com.RWI.Nidhi.user.serviceInterface.KycDetailsService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KycDetailsServiceImp implements KycDetailsService {

    @Autowired
    private KycDetailsRepo kycDetailsRepo;
    @Autowired
    UserRepo userRepo;

    @Override
    public KycDetailsDto saveKycDetails(KycDetailsDto kycDetailsDTO) {
        if(!userRepo.existsByEmail(kycDetailsDTO.getEmail()))return null;
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
        kycEntity.setNationality(kycDto.getNationality());
        kycEntity.setCategories(kycDto.getCategories());
        kycEntity.setOccupation(kycDto.getOccupation());
        kycEntity.setAlternatePhnNo(kycDto.getAlternatePhnNo());
        kycEntity.setNomineeFirstName(kycDto.getNomineeFirstName());
        kycEntity.setNomineeLastName(kycDto.getNomineeLastName());
        kycEntity.setNomineeContactNumber(kycDto.getNomineeContactNumber());
        kycEntity.setRelationWithNominee(kycDto.getRelationWithNominee());
        kycEntity.setOccupation(kycDto.getOccupation());
        kycEntity.setMartialStatus(kycDto.getMartialStatus());
        kycEntity.setMonthlyIncome(kycDto.getMonthlyIncome());
        kycEntity.setNumberOfFamilyMembers(kycDto.getNumberOfFamilyMembers());
        kycEntity.setReferralCode(kycDto.getReferralCode());
        kycEntity.setKycStatus(KycStatus.Pending);
        return kycEntity;
    }

    @Override
    public ResponseKycDto getSomeDetails(Long kycId) {
        KycDetails kycDetails = kycDetailsRepo.
                findById(kycId).orElseThrow(() -> new RuntimeException("Id not found"));
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
    public KycDetails getDetailsByUserEmail(String userEmail) {
        if (userRepo.existsByEmail(userEmail)) {
            User user = userRepo.findByEmail(userEmail);
            long kycId = user.getKyc().getKycId();
            return kycDetailsRepo.findById(kycId).orElseThrow(() -> new RuntimeException("Id not found:"));
        }
        else return null;
    }
}
