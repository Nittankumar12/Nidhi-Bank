package com.nidhi.kyc.KYC.Repo;

import com.nidhi.kyc.KYC.Entity.IdentityDocs;
import com.nidhi.kyc.KYC.Entity.KycDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KycDetailsRepo extends JpaRepository<KycDetails,Long> {
//    List<KycDetails> findSomeDetails(KycDetails kycDetails);
}
