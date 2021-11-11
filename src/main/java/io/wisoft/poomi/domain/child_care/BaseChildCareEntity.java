package io.wisoft.poomi.domain.child_care;

import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.global.exception.exceptions.NoPermissionOfContentException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public class BaseChildCareEntity extends BaseTimeEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "writer_id",
            referencedColumnName = "id"
    )
    private Member writer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "address_tag_id",
            referencedColumnName = "id"
    )
    private AddressTag addressTag;

    @Enumerated(EnumType.STRING)
    @Column(name = "recruitment_status")
    private RecruitmentStatus recruitmentStatus;

    protected BaseChildCareEntity(final Member writer, final AddressTag addressTag,
                                  final RecruitmentStatus recruitmentStatus) {
        this.writer = writer;
        this.addressTag = addressTag;
        this.recruitmentStatus = recruitmentStatus;
    }

    protected void setAddressTag(final AddressTag addressTag) {
        this.addressTag = addressTag;
    }

    protected void setWriter(final Member writer) {
        this.writer = writer;
    }

    public void isWriter(final Member member) {
        if (this.writer.equals(member)) {
            throw new IllegalArgumentException("작성자는 접근할 수 없는 요청입니다.");
        }
    }

    public void isNotWriter(final Member member) {
        if (!this.writer.equals(member)) {
            throw new NoPermissionOfContentException();
        }
    }

    protected void changeRecruitmentStatus(final RecruitmentStatus recruitmentStatus) {
        if (recruitmentStatus == null) {
            return;
        }
        this.recruitmentStatus = recruitmentStatus;
    }

    public void checkAccessPermission(final AddressTag addressTag) {
        if (!this.addressTag.equals(addressTag)) {
            throw new NoPermissionOfContentException();
        }
    }

    public void setClosed() {
        this.recruitmentStatus = RecruitmentStatus.CLOSED;
    }

}
