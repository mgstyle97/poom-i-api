package io.wisoft.poomi.domain.member.address;

import io.wisoft.poomi.domain.member.address.AddressTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressTagRepository extends JpaRepository<AddressTag, Long> {

    Optional<AddressTag> findByExtraAddress(String extraAddress);

    default AddressTag getAddressTagByExtraAddress(String extraAddress) {
        Optional<AddressTag> addressTag = this.findByExtraAddress(extraAddress);
        if (addressTag.isEmpty()) {
            AddressTag newAddressTag = new AddressTag(extraAddress);
            this.save(newAddressTag);

            return newAddressTag;
        }

        return addressTag.get();
    }
}
