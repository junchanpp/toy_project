package com.example.toy_project.dto.resopnse;

import com.example.toy_project.util.MembershipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddMembershipResponse {

  private Long id;
  private MembershipType membershipType;
}
