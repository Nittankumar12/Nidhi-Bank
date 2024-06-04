package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.Agent;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgentRepo extends JpaRepository<Agent, Integer> {
    Boolean existsByAgentEmail(String agentEmail);

    Agent findByAgentEmail(String agentEmail);

    boolean existsByAgentName(String email);

    boolean existsByAgentPhoneNum(String email);

    Agent findByAgentName(String username);

//    @Transactional
//    @Modifying
//    @Query(value = "select * from agent where agent.agentReferralCode = :agentReferralCode", nativeQuery = true)
    Agent findByReferralCode(String agentReferralCode);
}
