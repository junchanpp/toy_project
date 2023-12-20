package com.example.toy_project.repository;

import com.example.toy_project.entity.Membership;
import com.example.toy_project.util.MembershipType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Membership findByUserIdAndMembershipType(String userId, MembershipType membershipType);

    List<Membership> findByUserId(String userId);
}
