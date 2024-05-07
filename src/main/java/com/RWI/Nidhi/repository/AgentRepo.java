package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgentRepo extends JpaRepository<Agent,Integer> {
    @Query(value = "SELECT e FROM Agent e where e.agentEmail =:agentEmail")
    Optional<Agent> findByEmail(String agentEmail);
}
