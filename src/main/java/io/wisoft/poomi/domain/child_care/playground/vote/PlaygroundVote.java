package io.wisoft.poomi.domain.child_care.playground.vote;

import io.wisoft.poomi.domain.child_care.BaseTimeEntity;
import io.wisoft.poomi.domain.common.ApprovalStatus;
import io.wisoft.poomi.domain.file.UploadFile;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
@SequenceGenerator(
        name = "playground_vote_sequence_generator",
        sequenceName = "playground_vote_sequence",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "playground_vote")
public class PlaygroundVote extends BaseTimeEntity {

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

    @Column(name = "expired_validation_token")
    private String expiredValidationToken;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "address_id",
            referencedColumnName = "id"
    )
    private Address address;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "playground_vote_image",
            joinColumns = {@JoinColumn(name = "vote_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "image_id", referencedColumnName = "id")}
    )
    private Set<UploadFile> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "registrant_id",
            referencedColumnName = "id"
    )
    private Member registrant;

    @Builder
    public PlaygroundVote(final String purposeUsing,
                          final Address address, final Member registrant) {
        this.purposeUsing = purposeUsing;
        this.approvalStatus = ApprovalStatus.UN_APPROVED;
        this.expiredStatus = ExpiredStatus.CLOSED;
        this.address = address;
        this.registrant = registrant;
    }

    public void setImages(final Set<UploadFile> images) {
        this.images = images;
    }

    public void approveState(final String expiredValidationToken) {
        this.approvalStatus = ApprovalStatus.APPROVED;
        this.expiredStatus = ExpiredStatus.VOTING;
        this.expiredValidationToken = expiredValidationToken;
    }

}
