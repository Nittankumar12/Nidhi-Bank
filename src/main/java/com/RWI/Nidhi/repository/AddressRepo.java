package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address,Long> {
}
