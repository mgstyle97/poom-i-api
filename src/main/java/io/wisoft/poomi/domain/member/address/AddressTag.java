package io.wisoft.poomi.domain.member.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ADDRESS_TAG")
@SequenceGenerator(
        name = "ad_tag_sequence_generator",
        sequenceName = "ad_tag_sequence",
        initialValue = 1,
        allocationSize = 1
)
public class AddressTag {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator =
                    "ad_tag_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "extra_address")
    private String extraAddress;

    public AddressTag(final String extraAddress) {
        this.extraAddress = extraAddress;
    }

}
