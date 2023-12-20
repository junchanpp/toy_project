package com.example.toy_project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.toy_project.dto.request.GetMemberShipResponse;
import com.example.toy_project.dto.resopnse.AddMembershipResponse;
import com.example.toy_project.entity.Membership;
import com.example.toy_project.repository.MembershipRepository;
import com.example.toy_project.util.MembershipErrorResult;
import com.example.toy_project.util.MembershipException;
import com.example.toy_project.util.MembershipType;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

    private final String userId = "userId";
    private final MembershipType membershipType = MembershipType.NAVER;
    private final Integer point = 10000;

    @InjectMocks
    private MembershipService target;

    @Mock
    private MembershipRepository membershipRepository;

    @Test
    public void 멤버십등록실패_이미존재함() {
        // given
        doReturn(Membership.builder().build()).when(membershipRepository)
                .findByUserIdAndMembershipType(userId, membershipType);
        // when
        final MembershipException result = assertThrows(MembershipException.class,
                () -> target.addMembership(userId, membershipType, point));
        // then
        assertThat(result.getErrorResult()).isEqualTo(
                MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
    }

    @Test
    public void 멤버십등록성공() {
        //given
        doReturn(null).when(membershipRepository)
                .findByUserIdAndMembershipType(userId, membershipType);
        doReturn(membership()).when(membershipRepository).save(any(Membership.class));

        //when
        final AddMembershipResponse result = target.addMembership(userId, membershipType, point);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);

        verify(membershipRepository, times(1)).findByUserIdAndMembershipType(userId,
                membershipType);
        verify(membershipRepository, times(1)).save(any(Membership.class));
    }

    @Test
    public void 멤버십목록조회() {
        //given
        doReturn(List.of(membership())).when(membershipRepository).findByUserId(userId);

        //when
        final List<GetMemberShipResponse> result = target.getMemberShipList(userId);

        //then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(result.get(0).getPoint()).isEqualTo(10000);

        verify(membershipRepository, times(1)).findByUserId(userId);
    }

    private Membership membership() {
        return Membership.builder()
                .id(-1L)
                .userId(userId)
                .membershipType(MembershipType.NAVER)
                .point(point)
                .build();
    }
}
