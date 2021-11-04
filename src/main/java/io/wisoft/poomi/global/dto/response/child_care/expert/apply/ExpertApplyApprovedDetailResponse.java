package io.wisoft.poomi.global.dto.response.child_care.expert.apply;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.child.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ExpertApplyApprovedDetailResponse {

    @JsonProperty("member_id")
    private Long memberId;

    @JsonProperty("member_nick")
    private String memberNick;

    @JsonProperty("child_id")
    private Long childId;

    @JsonProperty("child_name")
    private String childName;

    @JsonProperty("child_birthday")
    @JsonFormat(pattern = "yyyy.MM.ss")
    private Date childBirthday;

    @Builder
    public ExpertApplyApprovedDetailResponse(final Long memberId, final String memberNick,
                                             final Long childId, final String childName, final Date childBirthday) {
        this.memberId = memberId;
        this.memberNick = memberNick;
        this.childId = childId;
        this.childName = childName;
        this.childBirthday = childBirthday;
    }

    public static ExpertApplyApprovedDetailResponse of(final Child child) {
        return ExpertApplyApprovedDetailResponse.builder()
                .memberId(child.getParent().getId())
                .memberNick(child.getParent().getNick())
                .childId(child.getId())
                .childName(child.getName())
                .childBirthday(child.getBirthday())
                .build();
    }

}
