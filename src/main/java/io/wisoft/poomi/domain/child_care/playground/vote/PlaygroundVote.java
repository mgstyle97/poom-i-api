package io.wisoft.poomi.domain.child_care.playground.vote;

import io.wisoft.poomi.domain.common.ApprovalStatus;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.Address;

import javax.persistence.*;

@Entity
@SequenceGenerator(
        name = "playground_vote_sequence_generator",
        sequenceName = "playground_vote_sequence",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "playground_vote")
public class PlaygroundVote {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "playground_vote_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "purpose_using")
    private String purposeUsing;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "expired_status")
    private ExpiredStatus expiredStatus;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "address_id",
            referencedColumnName = "id"
    )
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "registrant_id",
            referencedColumnName = "id"
    )
    private Member registrant;

}
