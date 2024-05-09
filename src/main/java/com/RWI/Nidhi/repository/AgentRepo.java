package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepo extends JpaRepository<Agent,Integer> {
    Boolean existsByAgentEmail(String agentEmail);
    Agent findByAgentEmail(String agentEmail);


    boolean existsByAgentName(String email);

    boolean existsByAgentPhoneNum(String email);

    Agent findByAgentName(String username);
}
