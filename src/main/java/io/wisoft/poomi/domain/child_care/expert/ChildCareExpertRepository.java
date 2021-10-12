package io.wisoft.poomi.domain.child_care.expert;

import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.global.exception.exceptions.NotFoundEntityDataException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChildCareExpertRepository extends JpaRepository<ChildCareExpert, Long> {

    List<ChildCareExpert> findAllByAddressTag(final AddressTag addressTag);

    default ChildCareExpert getById(final Long urgentId) {
        return this.findById(urgentId)
                .orElseThrow(
                        () -> new NotFoundEntityDataException("expert id: " + urgentId + "에 관한 데이터를 찾지 못했습니다.")
                );

    }

}
