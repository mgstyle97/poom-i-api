package io.wisoft.poomi.service;

import io.wisoft.poomi.bind.dto.ChildAddDto;
import io.wisoft.poomi.bind.dto.DeleteChildDto;
import io.wisoft.poomi.bind.request.ChildAddRequest;
import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.domain.member.child.ChildRepository;
import io.wisoft.poomi.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildService {

    private final MemberRepository memberRepository;
    private final ChildRepository childRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public ChildAddDto addChildren(List<ChildAddRequest> childAddRequest, HttpServletRequest request) {
        String email = jwtTokenProvider.getUsernameFromToken(jwtTokenProvider.resolveToken(request));

        Member member = memberRepository.getMemberByEmail(email);

        List<Child> children = new ArrayList<>();
        for (ChildAddRequest addRequest : childAddRequest) {
            Child child = Child.of(addRequest, childRepository);
            children.add(child);

        }
        member.setChildren(children);

        return ChildAddDto.of(member);
    }

    @Transactional
    public DeleteChildDto deleteChild(Long childId, HttpServletRequest request) {
        String email = jwtTokenProvider.getUsernameFromToken(jwtTokenProvider.resolveToken(request));

        Member member = memberRepository.getMemberByEmail(email);

        Child child = childRepository.getById(childId);
        member.removeChild(child);

        return new DeleteChildDto(childId, member.getId());
    }

}
