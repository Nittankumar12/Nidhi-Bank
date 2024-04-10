package com.Railworld.NidhiBankMonolithic.repo;

import com.Railworld.NidhiBankMonolithic.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member,Integer> {
    @Query("SELECT m FROM Member m WHERE m.company.cId = :companyId")
    List<Member> findAllByCId(int companyId);
}
