package io.wisoft.poomi.domain.child_care.group;

import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.global.exception.exceptions.NotFoundEntityDataException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChildCareGroupRepository extends JpaRepository<ChildCareGroup, Long> {

    Boolean existsByName(final String name);

    List<ChildCareGroup> findAllByAddressTag(final AddressTag addressTag);

    default ChildCareGroup getById(final Long groupId) {
        return this.findById(groupId).orElseThrow(
                () -> new NotFoundEntityDataException("group id: " + groupId + "에 관한 데이터를 찾지 못했습니다.")
        );
    }

}
