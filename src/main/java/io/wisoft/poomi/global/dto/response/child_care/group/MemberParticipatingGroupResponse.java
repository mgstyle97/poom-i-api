package io.wisoft.poomi.global.dto.response.child_care.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.child.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MemberParticipatingGroupResponse {

    @JsonProperty("member_id")
    private Long memberId;

    @JsonProperty("member_nick")
    private String memberNick;

    @JsonProperty("child_id")
    private Long childId;

    @JsonProperty("child_name")
    private String childName;

    @JsonProperty("child_birthday")
    @JsonFormat(pattern = "yyyy.MM.dd")
    private Date childBirthday;

    @Builder
    public MemberParticipatingGroupResponse(final Long memberId, final String memberNick,
                                            final Long childId, final String childName, final Date childBirthday) {
        this.memberId = memberId;
        this.memberNick = memberNick;
        this.childId = childId;
        this.childName = childName;
        this.childBirthday = childBirthday;
    }

    public static MemberParticipatingGroupResponse of(final Child participatingChild) {
        return MemberParticipatingGroupResponse.builder()
                .memberId(participatingChild.getParent().getId())
                .memberNick(participatingChild.getParent().getNick())
                .childId(participatingChild.getId())
                .childName(participatingChild.getName())
                .childBirthday(participatingChild.getBirthday())
                .build();
    }

    public static MemberParticipatingGroupResponse ofMember(final Member participatingMember) {
        return MemberParticipatingGroupResponse.builder()
                .memberId(participatingMember.getId())
                .memberNick(participatingMember.getNick())
                .build();
    }

    public void setChild(final Child child) {
        this.childId = child.getId();
        this.childName = child.getName();
        this.childBirthday = child.getBirthday();
    }

}
