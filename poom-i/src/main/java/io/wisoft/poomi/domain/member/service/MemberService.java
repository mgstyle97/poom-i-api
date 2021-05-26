package io.wisoft.poomi.domain.member.service;

import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.dto.MemberJoinDto;
import io.wisoft.poomi.domain.member.dto.MemberModifiedDto;
import io.wisoft.poomi.domain.member.dto.ModifiedProperties;
import io.wisoft.poomi.domain.member.exception.DuplicateMemberException;
import io.wisoft.poomi.domain.member.exception.WrongEmailPasswordException;
import io.wisoft.poomi.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member join(final MemberJoinDto memberJoinDto) {
        Optional<Member> member = memberRepository.findByEmail(memberJoinDto.getEmail());
        if (member.isEmpty()) {
            return memberRepository.save(
                    Member.of(memberJoinDto)
            );
        }else  {
            throw new DuplicateMemberException(member.get());
        }
    }

    public Member modified(final MemberModifiedDto memberModifiedDto) {
        Member member = memberRepository
                .findByEmail(memberModifiedDto.getEmail())
                .orElseThrow(NotFoundException::new);
        if (!Member.checkPassword(member, memberModifiedDto.getPassword())) {
            throw new WrongEmailPasswordException();
        }
        Member updatedMember = changeMemberInfo(member, memberModifiedDto);
        memberRepository.save(updatedMember);

        return updatedMember;
    }

    private Member changeMemberInfo(final Member member, final MemberModifiedDto memberModifiedDto) {
        ModifiedProperties modifiedProperties = memberModifiedDto.getModifiedProperties();

        member.setModifiedAt(LocalDateTime.now());

        return member
                .changeName(modifiedProperties.getName())
                .changePhoneNumber(modifiedProperties.getPhoneNumber())
                .changePassword(modifiedProperties.getPassword())
                .changeNick(modifiedProperties.getNick())
                .changeAddress(modifiedProperties.getAddress())
                .changeIsBabysitter(modifiedProperties.isBabysitter());
    }

}
