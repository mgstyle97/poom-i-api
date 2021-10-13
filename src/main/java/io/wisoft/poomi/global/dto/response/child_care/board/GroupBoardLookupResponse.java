package io.wisoft.poomi.global.dto.response.child_care.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.RecruitmentStatus;
import io.wisoft.poomi.domain.child_care.group.board.GroupBoard;
import io.wisoft.poomi.domain.child_care.group.image.Image;
import io.wisoft.poomi.global.utils.LocalDateTimeUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class GroupBoardLookupResponse {

    private String title;

    private String writer;

    @JsonProperty("recruitment_status")
    private RecruitmentStatus recruitmentStatus;

    @JsonProperty("regular_meeting_day")
    private String regularMeetingDay;

    private String contents;

    @JsonProperty("image_uris")
    private List<String> imageURIS;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("likes_count")
    private Integer likesCount;

    @JsonProperty("comment_count")
    private Integer commentCount;

    @Builder
    public GroupBoardLookupResponse(final GroupBoard board) {
        this.title = board.getChildCareGroup().getTitle();
        this.writer = board.getWriter().getNick();
        this.recruitmentStatus = board.getChildCareGroup().getRecruitmentStatus();
        this.regularMeetingDay = board.getChildCareGroup().getRegularMeetingDay();
        this.contents = board.getContents();
        this.imageURIS = board.getImageURIs();
        this.createdAt = LocalDateTimeUtils.getDateToString(board.getCreatedAt());
        this.likesCount = board.getLikes().size();
        this.commentCount = board.getComments().size();
    }

    public static GroupBoardLookupResponse of(final GroupBoard board) {
        return GroupBoardLookupResponse.builder()
                .board(board)
                .build();
    }

}
