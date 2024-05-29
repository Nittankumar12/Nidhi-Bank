package com.RWI.Nidhi.repository;

//import com.nidhi.kyc.KYC.Entity.IdentityDocs;
//import com.nidhi.kyc.KYC.Entity.KycDetails;
import com.RWI.Nidhi.entity.KycDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KycDetailsRepo extends JpaRepository<KycDetails,Long> {
    KycDetails findByEmail(String email);
//    List<KycDetails> findSomeDetails(KycDetails kycDetails);
}
