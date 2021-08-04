package io.wisoft.poomi.repository;

import io.wisoft.poomi.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByLoginId(String loginId);

    default Member getMemberByLoginId(String loginId) {
        Member member = this.findMemberByLoginId(loginId).orElseThrow(
                () -> new UsernameNotFoundException("No member data about login id")
        );

        return member;
    }

}
