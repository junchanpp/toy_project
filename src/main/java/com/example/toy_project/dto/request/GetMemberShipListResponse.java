package com.example.toy_project.dto.request;

import com.example.toy_project.entity.Membership;
import com.example.toy_project.util.MembershipType;
import lombok.Getter;

@Getter
public class GetMemberShipListResponse {

    private final MembershipType membershipType;
    private final Integer point;

    private GetMemberShipListResponse(MembershipType membershipType, Integer point) {
        this.membershipType = membershipType;
        this.point = point;
    }

    public static GetMemberShipListResponse from(Membership membership) {
        return new GetMemberShipListResponse(membership.getMembershipType(), membership.getPoint());
    }

}