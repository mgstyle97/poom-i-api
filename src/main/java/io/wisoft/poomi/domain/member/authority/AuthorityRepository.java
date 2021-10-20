package io.wisoft.poomi.domain.member.authority;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    default Authority getUserAuthority() {
        return this.findById(1L).get();
    }

    default Authority getAnonymousAuthority() {
        return this.findById(3L).get();
    }

}
