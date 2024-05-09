package com.RWI.Nidhi.Security.repository;


import com.RWI.Nidhi.Security.models.Credentials;
import com.RWI.Nidhi.entity.Admin;
import com.RWI.Nidhi.entity.Agent;
import com.RWI.Nidhi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialsRepo extends JpaRepository<Credentials, Long> {
    Optional<Credentials> findByUsername(String username);
    Credentials save(Agent agent);
    Credentials save(User user);
    Credentials save(Admin admin);

    Optional<Credentials> findByEmail(String email);

    Optional<Credentials> findByPhoneNumber(String phoneNumber);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);

    Optional<Credentials> findByUsernameOrEmailOrPhoneNumber(String username, String email, String phoneNumber);
}
