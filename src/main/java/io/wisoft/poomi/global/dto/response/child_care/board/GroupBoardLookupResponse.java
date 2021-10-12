package io.wisoft.poomi.global.dto.response.child_care.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.RecruitmentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupBoardLookupResponse {

    private String title;

    private String writer;

    @JsonProperty("recruitment_status")
    private RecruitmentStatus recruitmentStatus;

    @JsonProperty("regular_meeting_day")
    private String regularMeetingDay;



}
