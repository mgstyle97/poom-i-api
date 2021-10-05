package io.wisoft.poomi.domain.member.child;

import io.wisoft.poomi.global.exception.exceptions.NotFoundEntityDataException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRepository extends JpaRepository<Child, Long> {

    default Child getById(final Long childId) {
        return this.findById(childId).orElseThrow(
                () -> new NotFoundEntityDataException("child id: " + childId + "에 관한 데이터를 찾지 못했습니다.")
        );
    }

}
