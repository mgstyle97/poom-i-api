package io.wisoft.poomi.domain.child_care.playground.vote.voter;

import io.wisoft.poomi.domain.child_care.playground.vote.PlaygroundVote;
import io.wisoft.poomi.global.dto.response.child_care.playground.vote.AddressDetailResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "playground_voter_sequence_generator",
        sequenceName = "playground_voter_sequence",
        initialValue = 19,
        allocationSize = 1
)
@Table(name = "playground_voter")
public class PlaygroundVoter {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "playground_voter_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    private String dong;

    private String ho;

    @Enumerated(EnumType.STRING)
    @Column(name = "vote_type")
    private VoteType voteType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "vote_id",
            referencedColumnName = "id"
    )
    private PlaygroundVote playgroundVote;

    public int getDongOnlyNumber() {
        return Integer.parseInt(this.dong.replace("동", ""));
    }

    public int getHoOnlyNumber() {
        return Integer.parseInt(this.ho.replace("호", ""));
    }

    @Builder
    public PlaygroundVoter(final String dong, final String ho,
                           final PlaygroundVote playgroundVote) {
        this.dong = dong;
        this.ho = ho;
        this.voteType = VoteType.NOT_YET;
        this.playgroundVote = playgroundVote;
    }

    public static PlaygroundVoter of(final AddressDetailResponse.Juso juso,
                                     final PlaygroundVote playgroundVote) {
        return PlaygroundVoter.builder()
                .dong(juso.getDongNm())
                .ho(juso.getHoNm())
                .playgroundVote(playgroundVote)
                .build();
    }

    public void setVoteType(final VoteType voteType) {
        this.voteType = voteType;
    }

}
