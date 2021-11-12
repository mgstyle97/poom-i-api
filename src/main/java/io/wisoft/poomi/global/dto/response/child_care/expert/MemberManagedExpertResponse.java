package io.wisoft.poomi.global.dto.response.child_care.expert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.expert.ChildCareExpert;
import io.wisoft.poomi.domain.child_care.expert.apply.ChildCareExpertApply;
import io.wisoft.poomi.domain.common.ApprovalStatus;
import io.wisoft.poomi.global.dto.response.child_care.expert.apply.ExpertApplyApprovedDetailResponse;
import io.wisoft.poomi.global.dto.response.child_care.expert.apply.ExpertApplyDetailResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class MemberManagedExpertResponse {

    @JsonProperty("expert_id")
    private Long expertId;

    @JsonProperty("start_time")
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime startTime;

    @JsonProperty("end_time")
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime endTime;

    private String contents;

    @JsonProperty("parent_id")
    private Long parentId;

    @JsonProperty("parent_nick")
    private String parentNick;

    @JsonProperty("child_id")
    private Long childId;

    @JsonProperty("child_name")
    private String childName;

    @JsonProperty("child_birthday")
    @JsonFormat(pattern = "yyyy.MM.dd")
    private Date childBirthday;

    @Builder
    public MemberManagedExpertResponse(final Long expertId,
                                       final LocalDateTime startTime, final LocalDateTime endTime,
                                       final String contents,
                                       final Long parentId, final String parentNick,
                                       final Long childId, final String childName, final Date childBirthday) {
        this.expertId = expertId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.contents = contents;
        this.parentId = parentId;
        this.parentNick = parentNick;
        this.childId = childId;
        this.childName = childName;
        this.childBirthday = childBirthday;
    }

    public static MemberManagedExpertResponse of(final ChildCareExpert expert) {
        return MemberManagedExpertResponse.builder()
                .expertId(expert.getId())
                .startTime(expert.getStartTime())
                .endTime(expert.getEndTime())
                .contents(expert.getContents())
                .parentId(expert.getCaringChild().getParent().getId())
                .parentNick(expert.getCaringChild().getParent().getNick())
                .childId(expert.getCaringChild().getId())
                .childName(expert.getCaringChild().getName())
                .childBirthday(expert.getCaringChild().getBirthday())
                .build();
    }

}
