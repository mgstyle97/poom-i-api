package io.wisoft.poomi.domain.child_care.playground;

import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "playground_sequence_generator",
        sequenceName = "playground_sequence",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "child_care_playground")
public class ChildCarePlayground {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "playground_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    private String name;

    @Column(name = "operating_hours")
    private String operatingHours;

    private String holiday;

    @Column(name = "call_number")
    private String callNumber;

    @Column(columnDefinition = "TEXT")
    private String features;

    @OneToOne(fetch = FetchType.LAZY)
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
