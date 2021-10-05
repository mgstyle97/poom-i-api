package io.wisoft.poomi.domain.child_care.group;

import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.global.exception.exceptions.NotFoundEntityDataException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChildCareGroupRepository extends JpaRepository<ChildCareGroup, Long> {

    List<ChildCareGroup> findByAddressTag(final AddressTag addressTag);

    default ChildCareGroup getById(final Long classId) {
        return this.findById(classId).orElseThrow(
                () -> new NotFoundEntityDataException("class id: " + classId + "에 관한 데이터를 찾지 못했습니다.")
        );
    }

}
