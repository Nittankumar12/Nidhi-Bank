package com.RWI.Nidhi.user.repository;

import com.RWI.Nidhi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

}
