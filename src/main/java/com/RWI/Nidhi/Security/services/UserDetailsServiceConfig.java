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


        Credentials credentials = credRepository.findByUsernameOrEmailOrPhoneNumber(username,username,username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username/email/phone number: " + username));

        return UserDetailsImpl.buildUser(credentials);
    }
}
