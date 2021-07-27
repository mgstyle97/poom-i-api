package io.wisoft.poomi.domain.member.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@SequenceGenerator(
        name = "address_sequence_generator",
        sequenceName = "address_sequence",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "ADDRESS")
public class Address {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "address_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "ad_tag_id",
            referencedColumnName = "id"
    )
    private AddressTag addressTag;

    @Column(name = "detail_address")
    private String detailAddress;

}
