package io.wisoft.poomi.repository;

import io.wisoft.poomi.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByLoginId(String loginId);

}
