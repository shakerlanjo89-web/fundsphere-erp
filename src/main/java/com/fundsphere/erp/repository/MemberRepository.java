package com.fundsphere.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fundsphere.erp.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    long countByMembershipStatus(String membershipStatus);

    Member findByMemberCode(String memberCode);

}