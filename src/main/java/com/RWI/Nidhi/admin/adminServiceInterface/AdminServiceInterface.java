package com.RWI.Nidhi.admin.adminServiceInterface;

import com.RWI.Nidhi.dto.AddAgentDto;
import com.RWI.Nidhi.entity.Agent;
import java.util.List;

public interface AdminServiceInterface {

    Agent addAgent(AddAgentDto addAgentDto) throws Exception;
    Agent updateAgentName(int id,String agentName) throws Exception;
    Agent updateAgentAddress(int id, String agentAddress) throws Exception;
    Agent updateAgentEmail(int id,String agentEmail) throws Exception;
    Agent updateAgentPhoneNum(int id,String phoneNum) throws Exception;
    boolean deleteAgentById(int id) throws Exception;
    List<Agent> getAllAgents();
    Agent findAgentById(int id) throws Exception;
}
