package com.RWI.Nidhi.agent.serviceInterface;
import com.RWI.Nidhi.dto.Agentforgetpassword;
import com.RWI.Nidhi.dto.CommissionDto;
import com.RWI.Nidhi.enums.CommissionType;
import org.springframework.http.ResponseEntity;

import java.util.List;
public interface AgentServiceInterface {
    ResponseEntity<?> getAllUsers(String email);
    ResponseEntity<?> findUserById(int id,String agentEmail);
    ResponseEntity<String> agentForgetPasswordSendVerificationCode(String agentEmail) throws Exception;
    ResponseEntity<String> agentForgetPasswordVerifyVerificationCode(String agentEmail, String enteredOtp) throws Exception;
    ResponseEntity<?> updateAgentPassword(Agentforgetpassword agentforgetpassword) throws Exception;
    List<CommissionDto> getCommissionListByType(String agentEmail);
    List<CommissionDto> getCommissionListByType(String agentEmail, CommissionType commissionType);
}