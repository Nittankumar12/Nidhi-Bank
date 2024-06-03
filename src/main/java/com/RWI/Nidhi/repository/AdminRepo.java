package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminRepo extends JpaRepository<Admin, Integer> {
    Admin findByAdminName(String adminName);

    boolean existsByEmail(String email);

    boolean existsByAdminName(String username);

    boolean existsByPhoneNumber(String phoneNumber);
}
