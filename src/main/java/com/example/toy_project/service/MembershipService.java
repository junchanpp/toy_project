package com.example.toy_project.service;

import com.example.toy_project.dto.resopnse.AddMembershipResponse;
import com.example.toy_project.entity.Membership;
import com.example.toy_project.repository.MembershipRepository;
import com.example.toy_project.util.MembershipErrorResult;
import com.example.toy_project.util.MembershipException;
import com.example.toy_project.util.MembershipType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipService {

  private final MembershipRepository membershipRepository;

  public AddMembershipResponse addMembership(String userId, MembershipType membershipType,
      Integer point) {
    final Membership membership = membershipRepository
        .findByUserIdAndMembershipType(userId, membershipType);
    if (membership != null) {
      throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
    }
    final Membership newMembership = Membership.builder()
        .userId(userId)
        .membershipType(membershipType)
        .point(point)
        .build();

    final var savedMembership = membershipRepository.save(newMembership);

    return AddMembershipResponse.builder()
        .id(savedMembership.getId())
        .membershipType(savedMembership.getMembershipType())
        .build();
  }
}
