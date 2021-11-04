package io.wisoft.poomi.global.dto.response.child_care.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.group.participating.GroupParticipatingMember;
import io.wisoft.poomi.domain.member.child.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Optional;

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

    public static MemberParticipatingGroupResponse of(final GroupParticipatingMember groupParticipatingMember) {
        MemberParticipatingGroupResponse participatingGroup = MemberParticipatingGroupResponse.builder()
                .memberId(groupParticipatingMember.getMember().getId())
                .memberNick(groupParticipatingMember.getMember().getNick())
                .build();
        Optional.ofNullable(groupParticipatingMember.getChild()).ifPresent(participatingGroup::setChild);

        return participatingGroup;
    }

    public void setChild(final Child child) {
        this.childId = child.getId();
        this.childName = child.getName();
        this.childBirthday = child.getBirthday();
    }

}
