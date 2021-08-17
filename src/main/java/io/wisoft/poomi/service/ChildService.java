package io.wisoft.poomi.service;

import io.wisoft.poomi.bind.dto.ChildAddDto;
import io.wisoft.poomi.bind.dto.DeleteChildDto;
import io.wisoft.poomi.bind.request.ChildAddRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.domain.member.child.ChildRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChildService {

    private final ChildRepository childRepository;

    @Transactional
    public ChildAddDto addChildren(Member member, List<ChildAddRequest> childAddRequests) {
        List<Child> children = childAddRequests.stream()
                        .map(this::transferToChild)
                        .collect(Collectors.toList());
        log.info("Generate child through request data");

        member.setChildren(children);

        return ChildAddDto.of(member);
    }

    @Transactional
    public DeleteChildDto deleteChild(Long childId, Member member) {
        Child child = childRepository.getById(childId);
        log.info("Generate child data through child-id: {}", child.getId());

        member.removeChild(child);
        childRepository.delete(child);
        log.info("Delete child data: {}", childId);

        return new DeleteChildDto(childId, member.getId());
    }

    private Child transferToChild(ChildAddRequest childAddRequest) {
        return Child.of(childAddRequest, childRepository);
    }

}
