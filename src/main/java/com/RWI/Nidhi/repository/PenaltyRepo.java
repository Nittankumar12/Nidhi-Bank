package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PenaltyRepo extends JpaRepository<Penalty, Integer> {
}