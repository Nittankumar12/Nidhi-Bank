package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.MIS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MisRepo extends JpaRepository<MIS,Integer> {
}
