package io.wisoft.poomi.domain.member.child;

import io.wisoft.poomi.domain.member.child.Child;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {

    default Child getById(Long id) {
        Child child = this.findById(id).orElseThrow(
                () -> new IllegalArgumentException("No child data about id")
        );

        return child;
    }

}
