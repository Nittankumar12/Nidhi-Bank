package com.RWI.Nidhi.admin.adminServiceImplementation;

import com.RWI.Nidhi.admin.ResponseDto.AdminViewsAgentDto;
import com.RWI.Nidhi.admin.ResponseDto.AgentMinimalDto;
import com.RWI.Nidhi.admin.adminServiceInterface.AdminServiceInterface;
import com.RWI.Nidhi.dto.AddAgentDto;
import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.repository.AgentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImplementation implements AdminServiceInterface {
//    public List<Loan> getAllLoans() {
//        return new ArrayList<>();
//    }
//    public List<Agent> getAllAgents() {
//        return new ArrayList<>();
//    }
//
//    public List<Transactions> getAllTransactions() {
//        return new ArrayList<>();
//    }
//
//    public List<User> getAllUser() {
//        return new ArrayList<>();
//    }

    @Autowired
    AgentRepo agentRepo;
    @Override
    public Agent addAgent(AddAgentDto addAgentDto) throws Exception{
        Agent newAgent = new Agent();
        newAgent.setAgentName(addAgentDto.getAgentName());
        newAgent.setAgentEmail(addAgentDto.getAgentEmail());
        newAgent.setAgentPhoneNum(addAgentDto.getAgentPhoneNum());
        newAgent.setAgentPassword(addAgentDto.getAgentPassword());
        newAgent.setAgentAddress(addAgentDto.getAgentAddress());

        try {
            agentRepo.save(newAgent);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return newAgent;
    }

    @Override
    public Agent updateAgentName(int id, String agentName) throws Exception{
        Agent currAgent = agentRepo.findById(id).orElseThrow(() -> {return new Exception("agent not found");});

        currAgent.setAgentName(agentName);
        try {
            agentRepo.save(currAgent);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return currAgent;
    }

    @Override
    public Agent updateAgentAddress(int id, String agentAddress) throws Exception {
        Agent currAgent = agentRepo.findById(id).orElseThrow(() -> {return new Exception("agent not found");});

        currAgent.setAgentAddress(agentAddress);
        try {
            agentRepo.save(currAgent);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return currAgent;
    }

    @Override
    public Agent updateAgentEmail(int id, String agentEmail) throws Exception{
        Agent currAgent = agentRepo.findById(id).orElseThrow(() -> {return new Exception("agent not found");});

        currAgent.setAgentEmail(agentEmail);
        try {
            agentRepo.save(currAgent);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return currAgent;
    }

    @Override
    public Agent updateAgentPhoneNum(int id, String phoneNum) throws Exception {
        Agent currAgent = agentRepo.findById(id).orElseThrow(() -> {return new Exception("agent not found");});

        currAgent.setAgentPhoneNum(phoneNum);
        try {
            agentRepo.save(currAgent);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return currAgent;
    }

    @Override
    public boolean deleteAgentById(int id) throws Exception {
        try{
            agentRepo.deleteById(id);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public List<AgentMinimalDto> getAllAgents() {
        List<Agent> allAgents = agentRepo.findAll();
        List<AgentMinimalDto> idUsernameAgent = allAgents.stream()
                .map(agent -> new AgentMinimalDto(agent.getAgentId(),agent.getAgentName()))
                .collect(Collectors.toList());
        return idUsernameAgent;
    }

    @Override
    public AdminViewsAgentDto getAgentById(int id) throws Exception{
        Agent agent = agentRepo.findById(id).orElseThrow(() -> {return new Exception("agent not found");});
        AdminViewsAgentDto responseDto = new AdminViewsAgentDto();
        responseDto.setAgentId(agent.getAgentId());
        responseDto.setAgentName(agent.getAgentName());
        responseDto.setAgentPhoneNum(agent.getAgentPhoneNum());
        responseDto.setAgentEmail(agent.getAgentEmail());
        responseDto.setAgentAddress(agent.getAgentAddress());

        List<FixedDeposit> fdList = agent.getFixedDepositList();
        responseDto.setNumberOfFd(fdList.size());

        List<MIS> misList = agent.getMisList();
        responseDto.setNumberOfMis(misList.size());

        List<RecurringDeposit> rdList = agent.getRecurringDepositList();
        responseDto.setNumberOfRd(rdList.size());

        List<Scheme> schemeList = agent.getSchemeList();
        responseDto.setNumberOfScheme(schemeList.size());

        return responseDto;

    }

}
