package io.wisoft.poomi.domain.childminder.urgent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChildminderUrgentRepository extends JpaRepository<ChildminderUrgent, Long> {

    default ChildminderUrgent getById(final Long urgentId) {
        return this.findById(urgentId)
                .orElseThrow(
                        () -> new IllegalArgumentException("No childminder urgent data id: " + urgentId)
                );

    }

}
