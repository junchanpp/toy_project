package com.example.toy_project.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.toy_project.entity.Membership;
import com.example.toy_project.util.MembershipType;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@DataJpaTest
public class MembershipRepositoryTest {

  private final MembershipRepository membershipRepository;

  @Test
  public void 멤버십등록() {
    // given
    final Membership membership = Membership.builder()
        .userId("userId")
        .membershipType(MembershipType.NAVER)
        .point(10000)
        .build();

    // when
    final Membership result = membershipRepository.save(membership);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getUserId()).isEqualTo("userId");
    assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
    assertThat(result.getPoint()).isEqualTo(10000);
  }

  @Test
  public void 멤버십이존재하는지테스트() {
    // given
    final Membership membership = Membership.builder()
        .userId("userId")
        .membershipType(MembershipType.NAVER)
        .point(10000)
        .build();

    // when
    membershipRepository.save(membership);
    final Membership findResult = membershipRepository.findByUserIdAndMembershipType("userId",
        MembershipType.NAVER);

    // then
    assertThat(findResult).isNotNull();
    assertThat(findResult.getId()).isNotNull();
    assertThat(findResult.getUserId()).isEqualTo("userId");
    assertThat(findResult.getMembershipType()).isEqualTo(MembershipType.NAVER);
    assertThat(findResult.getPoint()).isEqualTo(10000);
  }
}
