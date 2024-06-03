package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.PermanentAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermanentAddressRepo  extends JpaRepository<PermanentAddress,Integer> {
}
