package io.wisoft.poomi.domain.childminder.classes;

import io.wisoft.poomi.domain.member.address.AddressTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChildminderClassRepository extends JpaRepository<ChildminderClass, Long> {

    List<ChildminderClass> findByAddressTag(final AddressTag addressTag);

    default ChildminderClass findChildminderClassById(final Long id) {
        ChildminderClass childminderClass = this.findById(id).orElseThrow(
                () -> new IllegalArgumentException("No class program data id= " + id)
        );

        return childminderClass;
    }

}
