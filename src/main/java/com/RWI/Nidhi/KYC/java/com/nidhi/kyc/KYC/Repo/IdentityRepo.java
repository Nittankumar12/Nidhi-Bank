package com.nidhi.kyc.KYC.Repo;

import com.nidhi.kyc.KYC.Entity.IdentityDocs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentityRepo extends JpaRepository<IdentityDocs,Integer> {
}
