package io.wisoft.poomi.service.member;

import io.wisoft.poomi.global.dto.response.member.ChildAddResponse;
import io.wisoft.poomi.global.dto.response.member.ChildDeleteResponse;
import io.wisoft.poomi.global.dto.request.member.ChildAddRequest;
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
    public List<ChildAddResponse> updateChildren(final Member member, final List<ChildAddRequest> childAddRequests) {

        member.setChildren(childAddRequests);
        childRepository.saveAll(member.getChildren());
        log.info("Generate child through request data and set");

        return member.getChildren().stream()
                .map(ChildAddResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChildDeleteResponse deleteChild(final Long childId, final Member member) {
        Child child = childRepository.getById(childId);
        log.info("Generate child data through child-id: {}", child.getId());

        member.removeChild(child);
        childRepository.delete(child);
        log.info("Delete child data: {}", childId);

        return new ChildDeleteResponse(childId, member.getId());
    }

}
