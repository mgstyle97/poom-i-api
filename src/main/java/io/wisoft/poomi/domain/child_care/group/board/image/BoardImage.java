package io.wisoft.poomi.domain.child_care.group.board.image;

import io.wisoft.poomi.domain.child_care.group.board.GroupBoard;
import io.wisoft.poomi.domain.image.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "board_image_sequence_generator",
        sequenceName = "board_image_sequence",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "board_image")
public class BoardImage {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "board_image_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "board_id",
            referencedColumnName = "id"
    )
    private GroupBoard board;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "image_id",
            referencedColumnName = "id"
    )
    private Image image;

    @Builder
    public BoardImage(final GroupBoard board,
                      final Image image) {
        this.board = board;
        this.image = image;
    }

}
