package com.RWI.Nidhi.admin.adminServiceInterface;

import com.RWI.Nidhi.admin.ResponseDto.AdminViewsAgentDto;
import com.RWI.Nidhi.admin.ResponseDto.AgentMinimalDto;
import com.RWI.Nidhi.dto.AddAgentDto;
import com.RWI.Nidhi.entity.Agent;

import java.util.List;

public interface AdminServiceInterface {

    Agent addAgent(AddAgentDto addAgentDto) throws Exception;

    Agent updateAgentName(int id, String agentName) throws Exception;

    Agent updateAgentAddress(int id, String agentAddress) throws Exception;

    Agent updateAgentEmail(int id, String agentEmail) throws Exception;

    Agent updateAgentPhoneNum(int id, String phoneNum) throws Exception;

    boolean deleteAgentById(int id) throws Exception;

    List<AgentMinimalDto> getAllAgents();

    AdminViewsAgentDto getAgentById(int id) throws Exception;


}
