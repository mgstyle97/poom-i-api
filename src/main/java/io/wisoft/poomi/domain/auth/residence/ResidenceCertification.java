package io.wisoft.poomi.domain.auth.residence;

import io.wisoft.poomi.domain.auth.BaseCertificationEntity;
import io.wisoft.poomi.domain.common.ApprovalStatus;
import io.wisoft.poomi.domain.file.UploadFile;
import io.wisoft.poomi.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@SequenceGenerator(
        name = "residence_certification_sequence_generator",
        sequenceName = "residence_certification_sequence",
        initialValue = 4,
        allocationSize = 1
)
@Table(name = "residence_certification")
public class ResidenceCertification extends BaseCertificationEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "residence_certification_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "member_id",
            referencedColumnName = "id"
    )
    private Member member;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "residence_file_id",
            referencedColumnName = "id"
    )
    private UploadFile residenceFile;

    public ResidenceCertification() {this(null, null, null);}

    @Builder
    public ResidenceCertification(final Member member,
                                  final UploadFile residenceFile,
                                  final String expiredValidationToken) {
        super(expiredValidationToken);
        this.member = member;
        this.residenceFile = residenceFile;
        this.approvalStatus = ApprovalStatus.UN_APPROVED;
    }

    public void approve(final String expiredValidationToken) {
        this.approvalStatus = ApprovalStatus.APPROVED;
        super.setExpiredValidationToken(expiredValidationToken);
    }

}
