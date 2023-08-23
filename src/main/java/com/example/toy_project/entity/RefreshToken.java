package com.example.toy_project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

  @JsonIgnore
  @Id
  @Column(name = "refresh_token_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long refreshTokenId;

  @JsonIgnore
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "refresh_token", length = 1000)
  private String refreshToken;

  @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private ZonedDateTime createdAt;
}
