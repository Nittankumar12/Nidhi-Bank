package com.RWI.Nidhi.repository;

//import com.nidhi.kyc.KYC.Entity.AddressProof;
import com.RWI.Nidhi.entity.AddressProof;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<AddressProof,Long> {
}
