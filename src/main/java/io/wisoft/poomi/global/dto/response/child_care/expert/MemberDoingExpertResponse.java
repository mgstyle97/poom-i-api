package io.wisoft.poomi.global.dto.response.child_care.expert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class MemberDoingExpertResponse {

    @JsonProperty("expert_id")
    private Long expertId;

    @JsonProperty("start_time")
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime startTime;

    @JsonProperty("end_time")
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime endTime;

    private String contents;

    @JsonProperty("member_id")
    private Long memberId;

    @JsonProperty("member_nick")
    private String memberNick;

    @JsonProperty("child_name")
    private String childName;

    @JsonProperty("child_birthday")
    @JsonFormat(pattern = "yyyy.MM.ss")
    private Date childBirthday;

    @Builder
    public MemberDoingExpertResponse(final Long expertId,
                                     final LocalDateTime startTime, final LocalDateTime endTime,
                                     final String contents,
                                     final Long memberId, final String memberNick,
                                     final String childName, final Date childBirthday) {
        this.expertId = expertId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.contents = contents;
        this.memberId = memberId;
        this.memberNick = memberNick;
        this.childName = childName;
        this.childBirthday = childBirthday;
    }


}
