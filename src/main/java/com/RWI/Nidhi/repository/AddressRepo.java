package com.nidhi.kyc.KYC.Repo;

import com.nidhi.kyc.KYC.Entity.AddressProof;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<AddressProof,Long> {
}
