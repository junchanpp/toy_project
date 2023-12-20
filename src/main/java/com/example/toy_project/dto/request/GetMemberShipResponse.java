package com.example.toy_project.dto.request;

import com.example.toy_project.entity.Membership;
import com.example.toy_project.util.MembershipType;
import lombok.Getter;

@Getter
public class GetMemberShipResponse {

    private final MembershipType membershipType;
    private final Integer point;

    private GetMemberShipResponse(MembershipType membershipType, Integer point) {
        this.membershipType = membershipType;
        this.point = point;
    }

    public static GetMemberShipResponse from(Membership membership) {
        return new GetMemberShipResponse(membership.getMembershipType(), membership.getPoint());
    }

}