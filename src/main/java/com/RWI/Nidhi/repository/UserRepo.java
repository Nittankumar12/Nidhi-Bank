package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
}
