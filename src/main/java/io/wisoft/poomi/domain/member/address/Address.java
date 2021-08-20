package io.wisoft.poomi.domain.member.address;

import io.wisoft.poomi.bind.request.AddressRegisterRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void of(final AddressRepository addressRepository,
                    final AddressTagRepository tagRepository,
                    final AddressRegisterRequest request) {
        this.postCode = request.getPostCode();
        this.detailAddress = request.getDetailAddress();
        this.addressTag = tagRepository.getAddressTagByExtraAddress(request.getExtraAddress());

        addressRepository.save(this);
    }

}
