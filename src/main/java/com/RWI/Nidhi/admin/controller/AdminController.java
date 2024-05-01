package com.RWI.Nidhi.admin.controller;

import com.RWI.Nidhi.admin.adminServiceImplementation.AdminServiceImplementation;
import com.RWI.Nidhi.dto.AddAgentDto;
import com.RWI.Nidhi.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminServiceImplementation adminService;

    @PostMapping("/addAgent")
    public Agent addAgent(@RequestBody AddAgentDto addAgentDto) throws Exception{
        return adminService.addAgent(addAgentDto);
    }
    @PutMapping("/updateAgentName")
    public Agent updateAgentName(@RequestParam("id") int id,@RequestParam("agentName") String agentName) throws Exception{
        return adminService.updateAgentName(id, agentName);
    }
    @PutMapping("/updateAgentAddress")
    public Agent updateAgentAddress(@RequestParam("id") int id,@RequestParam("agentAddress") String agentAddress) throws Exception{
        return adminService.updateAgentAddress(id, agentAddress);
    }
    @PutMapping("/updateAgentEmail")
    public Agent updateAgentEmail(@RequestParam("id") int id,@RequestParam("agentEmail") String agentEmail) throws Exception{
        return adminService.updateAgentEmail(id, agentEmail);
    }
    @PutMapping("/updateAgentPhone")
    public Agent updateAgentPhoneNum(int id,String phoneNum) throws Exception{
        return adminService.updateAgentPhoneNum(id, phoneNum);
    }
    @DeleteMapping("/deleteAgentById")
    public boolean deleteAgentById(@RequestParam("id") int id) throws Exception{
        return adminService.deleteAgentById(id);
    }
    @GetMapping("/getAllAgents")
    public List<Agent> getAllAgents(){
        return adminService.getAllAgents();
    }
    @GetMapping("/findAgentById")
    public Agent findAgentById(@RequestParam("id") int id) throws Exception{
        return adminService.findAgentById(id);
    }

}
