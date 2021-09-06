package io.wisoft.poomi.bind.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.program.classes.ClassProgram;
import io.wisoft.poomi.domain.program.classes.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ClassProgramSinglePageDto {

    @JsonProperty("class_id")
    private Long classId;

    private String title;

    private String contents;

    private String writer;

    @JsonProperty("is_recruit")
    private Boolean isRecruit;

    @JsonProperty("image_uris")
    private List<String> imageUris;

    private List<CommentSinglePageDto> comments;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    @Builder
    public ClassProgramSinglePageDto(final Long classId,
                                     final String title, final String contents, final String writer,
                                     final Boolean isRecruit, final List<String> imageUris,
                                     final List<CommentSinglePageDto> comments) {
        this.classId = classId;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.isRecruit = isRecruit;
        this.imageUris = imageUris;
        this.comments = comments;
        this.requestedAt = new Date();
    }

    public static ClassProgramSinglePageDto of(final ClassProgram classProgram, final String domainInfo) {
        return ClassProgramSinglePageDto.builder()
                .classId(classProgram.getId())
                .title(classProgram.getTitle())
                .contents(classProgram.getContents())
                .writer(classProgram.getWriter().getName())
                .isRecruit(classProgram.getIsRecruit())
                .imageUris(classProgram.getImages()
                        .stream()
                        .map(image -> domainInfo + "/api/image/" + image.getImageName())
                        .collect(Collectors.toList()))
                .comments(classProgram.getComments().stream()
                        .map(CommentSinglePageDto::of)
                        .collect(Collectors.toList()))
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
