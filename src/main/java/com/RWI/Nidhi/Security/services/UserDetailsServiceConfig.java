package com.RWI.Nidhi.Security.services;


import com.RWI.Nidhi.Security.models.Credentials;
import com.RWI.Nidhi.Security.repository.CredentialsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceConfig implements UserDetailsService {

    @Autowired
    private CredentialsRepo userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credentials credentials = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username/email/phone number: " + username));

        return UserDetailsImpl.buildUser(credentials);
    }
}
