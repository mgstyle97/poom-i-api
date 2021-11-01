package io.wisoft.poomi.domain.child_care.playground.vote;

import io.wisoft.poomi.domain.child_care.BaseTimeEntity;
import io.wisoft.poomi.domain.child_care.playground.vote.voter.PlaygroundVoter;
import io.wisoft.poomi.domain.child_care.playground.vote.voter.VoteType;
import io.wisoft.poomi.domain.common.ApprovalStatus;
import io.wisoft.poomi.domain.file.UploadFile;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.global.exception.exceptions.NoPermissionOfContentException;
import io.wisoft.poomi.global.exception.exceptions.NotApprovedVoteException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "playgroundVote")
    private Set<PlaygroundVoter> voters;

    @Override
    public int hashCode() {
        return this.id.intValue();
    }

    @Builder
    public PlaygroundVote(final String purposeUsing,
                          final Address address, final Member registrant) {
        this.purposeUsing = purposeUsing;
        this.approvalStatus = ApprovalStatus.UN_APPROVED;
        this.expiredStatus = ExpiredStatus.CLOSED;
        this.address = address;
        this.registrant = registrant;
        this.voters = new HashSet<>();
    }

    public void setImages(final Set<UploadFile> images) {
        this.images = images;
    }

    public void checkApprovalStatus() {
        if (this.approvalStatus.equals(ApprovalStatus.UN_APPROVED)) {
            throw new NotApprovedVoteException();
        }
    }

    public void approveState(final String expiredValidationToken) {
        this.approvalStatus = ApprovalStatus.APPROVED;
        this.expiredStatus = ExpiredStatus.VOTING;
        this.expiredValidationToken = expiredValidationToken;
    }

    public void setVoters(final Set<PlaygroundVoter> voters) {
        this.voters = voters;
    }

    public PlaygroundVoter getVoterByDongAndHo(final String dong, final String ho) {
        return this.voters.stream()
                .filter(voter ->
                        voter.getDong().equals(dong) && voter.getHo().equals(ho)
                ).findFirst().orElseThrow(NoPermissionOfContentException::new);
    }

    public Double calculateVotingRate() {
        double notVotingCount = getVotersNotVoting().size();

        return notVotingCount / this.voters.size() * 100;
    }

    public Double calculateAgreeRate() {
        double agreeVotingCount = getVoterAgree().size();

        return agreeVotingCount / this.voters.size() * 100;
    }

    public Double calculateDisagreeRate() {
        double disagreeVotingCount = getVoterDisagree().size();

        return disagreeVotingCount / this.voters.size() * 100;
    }

    public List<String> getVoterDongList() {
        return this.voters.stream()
                .map(PlaygroundVoter::getDong)
                .collect(Collectors.toList());
    }

    public Set<PlaygroundVoter> getVoterAgree() {
        return this.voters.stream()
                .filter(voter -> voter.getVoteType().equals(VoteType.AGREE))
                .collect(Collectors.toSet());
    }

    public Set<PlaygroundVoter> getVoterDisagree() {
        return this.voters.stream()
                .filter(voter -> voter.getVoteType().equals(VoteType.DISAGREE))
                .collect(Collectors.toSet());
    }

    public Set<PlaygroundVoter> getVotersNotVoting() {
        return this.voters.stream()
                .filter(voter -> voter.getVoteType().equals(VoteType.NOT_YET))
                .collect(Collectors.toSet());
    }

    public Map<String, List<String>> getNotVotingDongAndHo() {
        List<String> dongList = getVoterDongList();

        Map<String, List<String>> votingYetList = new HashMap<>();
        Set<PlaygroundVoter> notVotingVoters = getVotersNotVoting();
        dongList.forEach(dong -> {
            List<String> hoList = notVotingVoters.stream()
                    .filter(voter -> voter.getDong().equals(dong))
                    .map(PlaygroundVoter::getHo)
                    .collect(Collectors.toList());
            votingYetList.put(dong, hoList);
        });

        return votingYetList;
    }

}
