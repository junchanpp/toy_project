package com.example.toy_project.controller;

import static com.example.toy_project.util.MembershipConstants.USER_ID_HEADER;

import com.example.toy_project.dto.request.AddMembershipRequest;
import com.example.toy_project.dto.resopnse.AddMembershipResponse;
import com.example.toy_project.service.MembershipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MembershipController {

  private final MembershipService membershipService;

  @PostMapping(value = "/api/v1/memberships", consumes = "application/json", produces = "application/json")
  public ResponseEntity<AddMembershipResponse> addMembership(
      @RequestHeader(USER_ID_HEADER) final String userId,
      @RequestBody @Valid final AddMembershipRequest addMembershipRequest) {

    final var response = membershipService.addMembership(userId,
        addMembershipRequest.getMembershipType(),
        addMembershipRequest.getPoint());

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
