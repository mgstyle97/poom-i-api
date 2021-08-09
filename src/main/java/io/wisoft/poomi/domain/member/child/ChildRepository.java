package io.wisoft.poomi.domain.member.child;

import io.wisoft.poomi.domain.member.child.Child;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {

    Optional<Child> findByName(String name);

}
