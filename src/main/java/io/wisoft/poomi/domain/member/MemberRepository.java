package io.wisoft.poomi.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    boolean existsByEmail(final String email);
    boolean existsByNick(final String nick);

    default Member getMemberByEmail(final String email) {
        Member member = this.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("No member data about email")
        );

        return member;
    }

}
