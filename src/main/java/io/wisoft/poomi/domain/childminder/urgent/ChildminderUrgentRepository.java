package io.wisoft.poomi.domain.childminder.urgent;

import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.global.exception.exceptions.NotFoundEntityDataException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChildminderUrgentRepository extends JpaRepository<ChildminderUrgent, Long> {

    List<ChildminderUrgent> findAllByAddressTag(final AddressTag addressTag);

    default ChildminderUrgent getById(final Long urgentId) {
        return this.findById(urgentId)
                .orElseThrow(
                        () -> new NotFoundEntityDataException("urgent id: " + urgentId + "에 관한 데이터를 찾지 못했습니다.")
                );

    }

}
