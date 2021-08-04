package io.wisoft.poomi.domain.member.address;

import io.wisoft.poomi.bind.request.AddressRegisterRequest;
import io.wisoft.poomi.repository.AddressRepository;
import io.wisoft.poomi.repository.AddressTagRepository;
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

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "detail_address")
    private String detailAddress;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "ad_tag_id",
            referencedColumnName = "id"
    )
    private AddressTag addressTag;

    public static Address of(AddressRepository addressRepository,
                             AddressTagRepository tagRepository,
                             AddressRegisterRequest request) {
        Address address = Address.builder()
                .postCode(request.getPostCode())
                .detailAddress(request.getDetailAddress())
                .addressTag(tagRepository.getAddressTagByExtraAddress(request.getExtraAddress()))
                .build();

        addressRepository.save(address);
        return address;
    }

}
