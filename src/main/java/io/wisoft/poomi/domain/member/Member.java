package io.wisoft.poomi.domain.member;

import io.wisoft.poomi.domain.auth.residence.ResidenceCertification;
import io.wisoft.poomi.domain.child_care.expert.apply.ChildCareExpertApply;
import io.wisoft.poomi.domain.child_care.group.board.GroupBoard;
import io.wisoft.poomi.domain.child_care.group.participating.GroupParticipatingMember;
import io.wisoft.poomi.domain.child_care.playground.vote.PlaygroundVote;
import io.wisoft.poomi.domain.common.ApprovalStatus;
import io.wisoft.poomi.domain.file.UploadFile;
import io.wisoft.poomi.domain.member.evaluation.MemberEvaluation;
import io.wisoft.poomi.global.dto.request.member.SignupRequest;
import io.wisoft.poomi.domain.child_care.expert.ChildCareExpert;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.domain.member.authority.Authority;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Entity
@SequenceGenerator(
        name = "member_sequence_generator",
        sequenceName = "member_sequence",
        initialValue = 5,
        allocationSize = 1
)
@Table(name = "MEMBER")
public class Member {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "member_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nick")
    private String nick;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "age")
    private Integer age;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "profile_image_id",
            referencedColumnName = "id"
    )
    private UploadFile profileImage;

    @OneToMany(mappedBy = "member")
    private Set<MemberEvaluation> evaluations;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "member_authority",
            joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")}
    )
    private Set<Authority> authorities;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "address_id",
            referencedColumnName = "id"
    )
    private Address address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "residence_id",
            referencedColumnName = "id"
    )
    private ResidenceCertification residenceCertification;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "family_certification_file_id",
            referencedColumnName = "id"
    )
    private UploadFile familyCertificationFile;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "parent"
    )
    private Set<Child> children;

    @Embedded
    private ChildCareGroupProperties childCareGroupProperties;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "writer"
    )
    private Set<ChildCareExpert> writtenExpertContents;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "writer"
    )
    private Set<ChildCareExpertApply> expertApplies;

    @ManyToMany(
            fetch = FetchType.LAZY,
            mappedBy = "likes"
    )
    private Set<ChildCareExpert> likedExpertContents;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "manager")
    private ChildCareExpert managedExpertContent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "registrant")
    private Set<PlaygroundVote> registeredPlaygroundVotes;

    @Override
    public int hashCode() {
        return this.id.intValue();
    }

    @Builder
    public Member(final String name, final String phoneNumber,
                  final String email, final String password,
                  final String nick, final Integer age,
                  final Gender gender,
                  final Set<Authority> authorities,
                  final Address address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.nick = nick;
        this.age = age;
        this.evaluations = new HashSet<>();
        this.approvalStatus = ApprovalStatus.UN_APPROVED;
        this.gender = gender;
        this.authorities = authorities;
        this.address = address;
        this.children = new HashSet<>();
        this.childCareGroupProperties = new ChildCareGroupProperties();
        this.writtenExpertContents = new HashSet<>();
        this.expertApplies = new HashSet<>();
        this.likedExpertContents = new HashSet<>();
        this.registeredPlaygroundVotes = new HashSet<>();
    }

    public static Member of(final SignupRequest signupRequest,
                            final PasswordEncoder passwordEncoder,
                            final Authority userAuthority) {
        Member member = Member.builder()
                .name(signupRequest.getName())
                .phoneNumber(signupRequest.getPhoneNumber())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .nick(signupRequest.getNick())
                .age(signupRequest.getAge())
                .gender(signupRequest.getGender())
                .authorities(Collections.singleton(userAuthority))
                .build();
        return member;
    }

    public void setResidenceCertification(final ResidenceCertification certification) {
        this.residenceCertification = certification;
    }

    public void setFamilyCertificationFile(final UploadFile familyCertificationFile) {
        this.familyCertificationFile = familyCertificationFile;
    }

    public void approveSignup(final Authority authority) {
        this.approvalStatus = ApprovalStatus.APPROVED;
        this.authorities.add(authority);
    }

    public void approveResidence(final String expiredValidationToken) {
        this.residenceCertification.approve(expiredValidationToken);
    }

    public void saveProfileImage(final UploadFile profileImage) {
        this.profileImage = profileImage;
    }

    public int getAverageScore() {
        final int totalEvaluationCount = this.evaluations.size();
        if (totalEvaluationCount == 0) {
            return 0;
        }
        final int totalEvaluationScore = this.evaluations.stream()
                .mapToInt(MemberEvaluation::getScore)
                .sum();

        return totalEvaluationScore / totalEvaluationCount;
    }

    public void checkChildInChildren(final Child child) {
        if (!this.children.contains(child)) {
            throw new IllegalArgumentException("?????? ????????? ?????? ????????? ????????????.");
        }
    }

    public void updateAddressInfo(final Address address) {
        this.address = address;
    }

    public void setChildren(final List<Child> children) {
        if (children == null) {
            return;
        }
        this.children.addAll(children);
    }

    public Authentication toAuthentication() {
        Collection<? extends GrantedAuthority> authorities = this.authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(
                this.email, this.password, authorities
        );
    }

    public String getAuthority() {
        return this.authorities.stream()
                .findFirst()
                .get()
                .getAuthority();
    }

    public Member setOAuthAccountName(final String name) {
        this.name = name;
        return this;
    }

    public void removeChild(final Child child) {
        checkChildInChildren(child);
        this.children.remove(child);
    }

    public AddressTag getAddressTag() {
        return this.address.getAddressTag();
    }

    public void addParticipatingGroup(final GroupParticipatingMember participatingGroup) {
        this.childCareGroupProperties.getParticipatingGroups().add(participatingGroup);
    }

    public void withdrawFromGroup(final ChildCareGroup group) {
        this.childCareGroupProperties.getParticipatingGroups().stream()
                .filter(groupParticipatingMember -> groupParticipatingMember.getGroup().equals(group))
                .forEach(groupParticipatingMember -> {
                    this.childCareGroupProperties.getParticipatingGroups().remove(groupParticipatingMember);
                });
    }

    public void addWrittenExpertContent(final ChildCareExpert expertContent) {
        this.writtenExpertContents.add(expertContent);
    }

    public void manageOfExpertContent(final ChildCareExpert expertContent) {
        this.managedExpertContent = expertContent;
    }

    public void addExpertApply(final ChildCareExpertApply expertApply) {
        this.expertApplies.add(expertApply);
    }

    public void addLikedExpertContent(final ChildCareExpert expertContent) {
        this.likedExpertContents.add(expertContent);
    }

    public void addEvaluation(final MemberEvaluation evaluation) {
        this.evaluations.add(evaluation);
    }

    public void removeLikedExpertContent(final ChildCareExpert expertContent) {
        this.likedExpertContents.remove(expertContent);
    }

    public void removeExpertApply(final ChildCareExpertApply expertApply) {
        this.expertApplies.remove(expertApply);
    }

    public void removeWrittenExpertContent(final ChildCareExpert expertContent) {
        this.writtenExpertContents.remove(expertContent);
    }

    public void cancelExpert() {
        this.managedExpertContent = null;
    }

    public void addLikeBoard(final GroupBoard board) {
        this.childCareGroupProperties.getLikeBoards().add(board);
    }

    public void removeLikeBoard(final GroupBoard board) {
        this.childCareGroupProperties.getLikeBoards().remove(board);
    }

    public void addPlaygroundVote(final PlaygroundVote playgroundVote) {
        this.registeredPlaygroundVotes.add(playgroundVote);
    }
}
