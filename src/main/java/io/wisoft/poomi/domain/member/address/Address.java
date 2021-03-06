package io.wisoft.poomi.domain.member.address;

import io.wisoft.poomi.global.dto.request.member.AddressRegisterRequest;
import io.wisoft.poomi.global.dto.request.member.SignupRequest;
import lombok.*;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "address_sequence_generator",
        sequenceName = "address_sequence",
        initialValue = 7,
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

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "address")
    private String address;

    @Column(name = "detail_address")
    private String detailAddress;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "address_tag_id",
            referencedColumnName = "id"
    )
    private AddressTag addressTag;

    @Builder
    public Address(final String postCode, final String address,
                   final String detailAddress, final AddressTag addressTag) {
        this.postCode = postCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.addressTag = addressTag;
    }

    public static Address of(final SignupRequest request,
                             final AddressTag addressTag) {
        Address address = Address.builder()
                .postCode(request.getPostCode())
                .address(request.getAddress())
                .detailAddress(request.getDetailAddress())
                .addressTag(addressTag)
                .build();

        return address;
    }

}
