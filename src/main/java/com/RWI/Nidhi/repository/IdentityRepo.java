package com.RWI.Nidhi.repository;

//import com.nidhi.kyc.KYC.Entity.IdentityDocs;
import com.RWI.Nidhi.entity.IdentityDocs;
import com.RWI.Nidhi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentityRepo extends JpaRepository<IdentityDocs,Integer> {
    IdentityDocs findByUser(User user);
}

