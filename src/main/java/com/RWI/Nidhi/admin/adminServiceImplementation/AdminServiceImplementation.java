package com.RWI.Nidhi.admin.adminServiceImplementation;

import com.RWI.Nidhi.admin.adminServiceInterface.AdminServiceInterface;
import com.RWI.Nidhi.dto.AddAgentDto;
import com.RWI.Nidhi.entity.Agent;
import com.RWI.Nidhi.repository.AgentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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
    public List<Agent> getAllAgents() {
        return agentRepo.findAll();
    }

    @Override
    public Agent findAgentById(int id) throws Exception{
        return agentRepo.findById(id).orElseThrow(() -> {return new Exception("agent not found");});
    }

}
