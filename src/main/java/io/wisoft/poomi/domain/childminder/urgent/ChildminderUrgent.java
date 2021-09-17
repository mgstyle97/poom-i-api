package io.wisoft.poomi.domain.childminder.urgent;

import io.wisoft.poomi.global.dto.request.childminder.urgent.ChildminderUrgentModifiedRequest;
import io.wisoft.poomi.global.dto.request.childminder.urgent.ChildminderUrgentRegisterRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.childminder.BaseTimeEntity;
import io.wisoft.poomi.domain.member.address.AddressTag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "urgent_sequence_generator",
        sequenceName = "urgent_sequence",
        initialValue = 1,
        allocationSize = 1
)
public class ChildminderUrgent extends BaseTimeEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "urgent_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @Column(
            name = "contents",
            columnDefinition = "TEXT"
    )
    private String contents;

    @Column(name = "is_recruit")
    private Boolean isRecruit;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "member_id",
            referencedColumnName = "id"
    )
    private Member writer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "address_tag_id",
            referencedColumnName = "id"
    )
    private AddressTag addressTag;

    @Builder
    public ChildminderUrgent(final String contents, final Boolean isRecruit,
                             final LocalDateTime startTime, final LocalDateTime endTime,
                             final Member writer) {
        this.contents = contents;
        this.isRecruit = isRecruit;
        this.startTime = startTime;
        this.endTime = endTime;
        this.writer = writer;
        this.addressTag = writer.getAddressTag();
    }

    public static ChildminderUrgent of(final ChildminderUrgentRegisterRequest childminderUrgentRegisterRequest,
                                       final Member member) {
        ChildminderUrgent childminderUrgent = ChildminderUrgent.builder()
                .contents(childminderUrgentRegisterRequest.getContents())
                .isRecruit(childminderUrgentRegisterRequest.getIsRecruit())
                .startTime(childminderUrgentRegisterRequest.getStartTime())
                .endTime(childminderUrgentRegisterRequest.getEndTime())
                .writer(member)
                .build();
        member.addUrgent(childminderUrgent);

        return childminderUrgent;
    }

    public void modifiedFor(final ChildminderUrgentModifiedRequest childminderUrgentModifiedRequest) {
        changeContents(childminderUrgentModifiedRequest.getContents());
        changeIsRecruit(childminderUrgentModifiedRequest.getIsRecruit());
        changeStartTime(childminderUrgentModifiedRequest.getStartTime());
        changeEndTime(childminderUrgentModifiedRequest.getEndTime());
    }

    private void changeContents(final String contents) {
        if(!StringUtils.hasText(contents)) {
            return;
        }
        this.contents = contents;
    }

    private void changeIsRecruit(final Boolean isRecruit) {
        if (this.isRecruit.equals(isRecruit)) {
            return;
        }
        this.isRecruit = isRecruit;
    }

    private void changeStartTime(final LocalDateTime startTime) {
        if(this.startTime.equals(startTime)) {
            return;
        }

        this.startTime = startTime;
    }

    private void changeEndTime(final LocalDateTime endTime) {
        if (this.endTime.equals(endTime)) {
            return;
        }

        this.endTime = endTime;
    }

}
