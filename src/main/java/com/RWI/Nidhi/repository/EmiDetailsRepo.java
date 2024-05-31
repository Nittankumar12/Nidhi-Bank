package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.EmiDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmiDetailsRepo extends JpaRepository<EmiDetails, Integer> {
}
