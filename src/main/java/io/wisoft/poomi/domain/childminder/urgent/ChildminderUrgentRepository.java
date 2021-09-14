package io.wisoft.poomi.domain.childminder.urgent;

import io.wisoft.poomi.domain.member.address.AddressTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChildminderUrgentRepository extends JpaRepository<ChildminderUrgent, Long> {

    List<ChildminderUrgent> findAllByAddressTag(final AddressTag addressTag);

    default ChildminderUrgent getById(final Long urgentId) {
        return this.findById(urgentId)
                .orElseThrow(
                        () -> new IllegalArgumentException("No childminder urgent data id: " + urgentId)
                );

    }

}
