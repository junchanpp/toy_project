package com.example.toy_project.dto.request;

import com.example.toy_project.entity.Membership;
import com.example.toy_project.util.MembershipType;
import java.util.List;

public class GetMemberShipListResponse {

    private MembershipType membershipType;
    private Integer point;

    public GetMemberShipListResponse(MembershipType membershipType, Integer point) {
        this.membershipType = membershipType;
        this.point = point;
    }

    public static List<GetMemberShipListResponse> from(List<Membership> byUserId) {
        return byUserId.stream()
                .map(membership -> new GetMemberShipListResponse(membership.getMembershipType(),
                        membership.getPoint()))
                .toList();
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public Integer getPoint() {
        return point;
    }
}