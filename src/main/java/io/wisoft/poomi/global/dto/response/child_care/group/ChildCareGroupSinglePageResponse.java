package io.wisoft.poomi.global.dto.response.child_care.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.RecruitmentStatus;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ChildCareGroupSinglePageResponse {

    @JsonProperty("group_id")
    private Long groupId;

    private String title;

    private String contents;

    private String writer;

    @JsonProperty("recruitment_status")
    private RecruitmentStatus recruitmentStatus;

    @JsonProperty("image_uris")
    private List<String> imageUris;

    private List<CommentSinglePageDto> comments;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    @Builder
    private ChildCareGroupSinglePageResponse(final Long groupId,
                                             final String title, final String contents, final String writer,
                                             final RecruitmentStatus recruitmentStatus, final List<String> imageUris,
                                             final List<CommentSinglePageDto> comments) {
        this.groupId = groupId;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.recruitmentStatus = recruitmentStatus;
        this.imageUris = imageUris;
        this.comments = comments;
        this.requestedAt = new Date();
    }

    public static ChildCareGroupSinglePageResponse of(final ChildCareGroup childCareGroup) {
        return ChildCareGroupSinglePageResponse.builder()
                .groupId(childCareGroup.getId())
                .title(childCareGroup.getName())
                .writer(childCareGroup.getWriter().getName())
                .recruitmentStatus(childCareGroup.getRecruitmentStatus())
                .build();
    }

    @Getter
    @Setter
    public static class CommentSinglePageDto {

        @JsonProperty("comment_id")
        private Long commentId;

        private String contents;

        private String writer;

        @JsonProperty("created_at")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date createdAt;

        @Builder
        public CommentSinglePageDto(final Long commentId,
                                    final String contents, final String writer,
                                    final LocalDateTime createdAt) {
            this.commentId = commentId;
            this.contents = contents;
            this.writer = writer;
            this.createdAt = Date.from(createdAt.atZone(ZoneId.systemDefault()).toInstant());
        }

        public static CommentSinglePageDto of(final Comment comment) {
            return CommentSinglePageDto.builder()
                    .commentId(comment.getId())
                    .contents(comment.getContents())
                    .writer(comment.getWriter().getName())
                    .createdAt(comment.getCreatedAt())
                    .build();
        }

    }

}
