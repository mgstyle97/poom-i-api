package io.wisoft.poomi.global.dto.response.child_care.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.RecruitmentStatus;
import io.wisoft.poomi.domain.child_care.group.board.GroupBoard;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.response.child_care.LikeStatus;
import io.wisoft.poomi.global.utils.LocalDateTimeUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class GroupBoardLookupResponse {

    @JsonProperty("board_id")
    private Long boardId;

    @JsonProperty("group_id")
    private Long groupId;

    private String title;

    private String writer;

    @JsonProperty("recruitment_status")
    private RecruitmentStatus recruitmentStatus;

    @JsonProperty("regular_meeting_day")
    private String regularMeetingDay;

    private String contents;

    @JsonProperty("images")
    private List<ImageLookupResponse> imageURIS;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("likes_count")
    private Integer likesCount;

    @JsonProperty("comment_count")
    private Integer commentCount;

    @JsonProperty("like_status")
    private LikeStatus likeStatus;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    @Builder
    public GroupBoardLookupResponse(final GroupBoard board, final Member member) {
        this.boardId = board.getId();
        this.groupId = board.getChildCareGroup().getId();
        this.title = board.getChildCareGroup().getName();
        this.writer = board.getWriter().getNick();
        this.recruitmentStatus = board.getChildCareGroup().getRecruitmentStatus();
        this.regularMeetingDay = board.getChildCareGroup().getRegularMeetingDay();
        this.contents = board.getContents();
        this.imageURIS = board.getImages().stream()
                .map(ImageLookupResponse::of)
                .collect(Collectors.toList());
        this.createdAt = LocalDateTimeUtils.getDateToString(board.getCreatedAt());
        this.likesCount = board.getLikes().size();
        this.commentCount = board.getComments().size();
        this.likeStatus = LikeStatus.generateLikeStatue(board.getLikes(), member);
        this.requestedAt = new Date();
    }

    public static GroupBoardLookupResponse of(final GroupBoard board, final Member member) {
        return GroupBoardLookupResponse.builder()
                .board(board)
                .member(member)
                .build();
    }

}
