package com.example.toy_project.repository;

import com.example.toy_project.entity.Membership;
import com.example.toy_project.util.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

  Membership findByUserIdAndMembershipType(String userId, MembershipType membershipType);
}
