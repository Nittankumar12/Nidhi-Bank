package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.ResidentialAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResidentialAddressRepo extends JpaRepository<ResidentialAddress,Integer> {
}
