package com.Railworld.NidhiBankMonolithic.repo;

import com.Railworld.NidhiBankMonolithic.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
    Account findByAccountNo(Long accountNo);
}
