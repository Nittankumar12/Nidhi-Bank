package com.RWI.Nidhi.Security.repository;


import com.RWI.Nidhi.Security.models.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialsRepo extends JpaRepository<Credentials, Long> {
    Optional<Credentials> findByUsername(String username);

    Optional<Credentials> findByEmail(String email);

    Optional<Credentials> findByPhoneNumber(String phoneNumber);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);

    Optional<Credentials> findByUsernameOrEmailOrPhoneNumber(String username, String email, String phoneNumber);
}
