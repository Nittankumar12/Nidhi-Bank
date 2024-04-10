package com.Railworld.NidhiBankMonolithic.repo;

import com.Railworld.NidhiBankMonolithic.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Integer> {
    void deleteByAccountId(int accountId);
}
