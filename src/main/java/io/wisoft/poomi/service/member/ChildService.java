package io.wisoft.poomi.service.member;

import io.wisoft.poomi.global.dto.request.member.ChildSimpleDataResponse;
import io.wisoft.poomi.global.dto.response.member.ChildAddResponse;
import io.wisoft.poomi.global.dto.response.member.ChildDeleteResponse;
import io.wisoft.poomi.global.dto.request.member.ChildAddRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.domain.member.child.ChildRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChildService {

    private final ChildRepository childRepository;

    @Transactional(readOnly = true)
    public List<ChildSimpleDataResponse> childSimpleList(final Member member) {
        Set<Child> children = member.getChildren();

        return children.stream()
                .map(ChildSimpleDataResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ChildAddResponse> updateChildren(final Member member, final List<ChildAddRequest> childAddRequests) {

        List<Child> children = childAddRequests.stream()
                        .map(childAddRequest -> Child.of(childAddRequest, member))
                                .collect(Collectors.toList());
        childRepository.saveAll(children);
        member.setChildren(children);
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

    @Transactional(readOnly = true)
    public Child checkChildId(
            final Member member,
            final Optional<Long> optionalChildId) {

        if (optionalChildId.isPresent()) {
            Child child = childRepository.getById(optionalChildId.get());
            member.checkChildInChildren(child);

            return child;
        }

        return null;
    }

}
