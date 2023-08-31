package com.example.toy_project.dto.request;

import com.example.toy_project.util.MembershipType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddMembershipRequest {

  @NotNull
  @Min(0)
  private Integer point;

  @NotNull
  private MembershipType membershipType;
}
