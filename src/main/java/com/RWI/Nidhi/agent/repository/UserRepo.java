package com.RWI.Nidhi.agent.repository;

import com.RWI.Nidhi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
}
