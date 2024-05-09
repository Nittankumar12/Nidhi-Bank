package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
	boolean existsByEmail(String email);
//	@Query(value = "SELECT e FROM Agent e where e.email =:email")
	Optional<User> findUserByEmail(String email);

	 User findByEmail(String email);
	 boolean existsByUserName(String username);

	boolean existsByPhoneNumber(String phoneNumber);

	User findByUserName(String username);
}
