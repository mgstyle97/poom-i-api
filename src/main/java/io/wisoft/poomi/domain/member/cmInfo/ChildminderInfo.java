package io.wisoft.poomi.domain.member.cmInfo;

import io.wisoft.poomi.global.dto.request.member.CMInfoRegisterRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@SequenceGenerator(
        name = "childminder_info_seq_generator",
        sequenceName = "childminder_info_seq",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "CHILDMINDER_INFO")
public class ChildminderInfo {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "childminder_info_seq_generator"
    )
    @Column(name = "id")
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "experience")
    private String experience;

    @Column(name = "greeting")
    private String greeting;

    @Column(name = "score")
    private int score;

    public static ChildminderInfo from(final CMInfoRegisterRequest request) {
        ChildminderInfo childminderInfo = new ChildminderInfo();
        BeanUtils.copyProperties(request, childminderInfo);

        return childminderInfo;
    }
}
