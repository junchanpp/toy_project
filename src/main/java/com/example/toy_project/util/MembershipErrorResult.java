package com.example.toy_project.util;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

;

@Getter
@RequiredArgsConstructor
public enum MembershipErrorResult {
  DUPLICATED_MEMBERSHIP_REGISTER(HttpStatus.BAD_REQUEST, "이미 존재하는 멤버십입니다."),
  UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown Exception"),
  ;
  private final HttpStatus httpStatus;
  private final String message;
}

