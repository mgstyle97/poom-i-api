package io.wisoft.poomi.domain.board;

import io.wisoft.poomi.domain.board.dto.BoardPostDto;
import io.wisoft.poomi.domain.comment.Comment;
import io.wisoft.poomi.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@SequenceGenerator(
        name = "board_sequence_generator",
        sequenceName = "board_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class Board {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "board_sequence_generator"
    )
    @Column(name = "board_id")
    private Long id;

    private String title;

    private String contents;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    private int views;

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();

    public static Board of(final Member member, final BoardPostDto boardPostDto) {
        return Board.builder()
                .title(boardPostDto.getTitle())
                .contents(boardPostDto.getContents())
                .createdAt(LocalDateTime.now())
                .member(member)
                .build();
    }

    public Board changeTitle(final String title) {
        if ((title != null) &&
                (!this.title.equals(title))) {
            this.title = title;
        }

        return this;
    }

    public Board changeContents(final String contents) {
        if ((contents != null) &&
                (!this.contents.equals(contents))) {
            this.contents = contents;
        }

        return this;
    }

    public void increaseViews() {
        this.views++;
    }

}
