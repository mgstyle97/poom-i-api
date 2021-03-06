package io.wisoft.poomi.domain.member;

import io.wisoft.poomi.global.exception.exceptions.NotFoundEntityDataException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByNick(String nick);
    boolean existsByEmail(final String email);
    boolean existsByNick(final String nick);

    default Member getMemberByEmail(final String email) {
        return this.findByEmail(email).orElseThrow(
                () -> new NotFoundEntityDataException("email: " + email + "에 관한 데이터를 찾지 못했습니다.")
        );
    }

}
