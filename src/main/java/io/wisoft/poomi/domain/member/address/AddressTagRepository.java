package io.wisoft.poomi.domain.member.address;

import io.wisoft.poomi.domain.member.address.AddressTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressTagRepository extends JpaRepository<AddressTag, Long> {

    Optional<AddressTag> findByExtraAddress(final String extraAddress);
    boolean existsAddressTagByExtraAddress(final String extraAddress);

    default AddressTag getAddressTagByExtraAddress(final String extraAddress) {
        Optional<AddressTag> addressTag = this.findByExtraAddress(extraAddress);
        if (addressTag.isEmpty()) {
            AddressTag newAddressTag = new AddressTag(extraAddress);
            this.save(newAddressTag);

            return newAddressTag;
        }

        return addressTag.get();
    }

    default AddressTag saveAddressTagWithExtraAddress(final String extraAddress) {
        if (this.existsAddressTagByExtraAddress(extraAddress)) {
            return this.findByExtraAddress(extraAddress).get();
        }

        return this.save(new AddressTag(extraAddress));
    }
}
