package com.RWI.Nidhi.Security.services;


import com.RWI.Nidhi.Security.models.Credentials;
import com.RWI.Nidhi.Security.repository.CredentialsRepo;
import com.RWI.Nidhi.entity.Admin;
import com.RWI.Nidhi.entity.Agent;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.repository.AdminRepo;
import com.RWI.Nidhi.repository.AgentRepo;
import com.RWI.Nidhi.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceConfig implements UserDetailsService {

    @Autowired
    private CredentialsRepo credRepository;
    @Autowired
    AdminRepo adminRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    AgentRepo agentRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin admin = adminRepo.findByAdminName(username);
        User user = userRepo.findByUserName(username);
        Agent agent = agentRepo.findByAgentName(username);
        Credentials credentials = null;
        if(agent!=null){
            credentials = new Credentials(agent.getAgentId(),agent.getAgentName(),agent.getAgentEmail(),agent.getAgentPhoneNum(),agent.getAgentPassword(),agent.getRoles());
        }
        if(user!=null){
            credentials = new Credentials(user.getUserId(),user.getUserName(),user.getEmail(),user.getPhoneNumber(),user.getPassword(),user.getRoles());
        }
        if(admin!=null){
            credentials = new Credentials(admin.getAdminId(),admin.getAdminName(),admin.getEmail(),admin.getPhoneNumber(),admin.getPassword(),admin.getRoles());
        }
        return UserDetailsImpl.buildUser(credentials);
    }
}
