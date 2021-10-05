package io.wisoft.poomi.domain.child_care.expert.apply;

import io.wisoft.poomi.global.exception.exceptions.NotFoundEntityDataException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildCareExpertApplyRepository extends JpaRepository<ChildCareExpertApply, Long> {

    default ChildCareExpertApply getById(final Long applyId) {
        return this.findById(applyId)
                .orElseThrow(
                () -> new NotFoundEntityDataException("apply id: " + applyId + "에 관한 데이터를 찾지 못했습니다.")
        );
    }

}
