package io.wisoft.poomi.domain.child_care.playground.vote.voter;

import io.wisoft.poomi.domain.child_care.playground.vote.PlaygroundVote;

import javax.persistence.*;

@Entity
@SequenceGenerator(
        name = "playground_voter_sequence_generator",
        sequenceName = "playground_voter_sequence",
        initialValue = 1,
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

    @Column(name = "vote_type")
    private VoteType voteType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "vote_id",
            referencedColumnName = "id"
    )
    private PlaygroundVote playgroundVote;

}
