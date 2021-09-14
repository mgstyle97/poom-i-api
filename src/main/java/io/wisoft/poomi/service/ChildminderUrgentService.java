package io.wisoft.poomi.service;

import io.wisoft.poomi.bind.dto.childminder.urgent.ChildminderUrgentLookupDto;
import io.wisoft.poomi.bind.dto.childminder.urgent.ChildminderUrgentModifiedDto;
import io.wisoft.poomi.bind.dto.childminder.urgent.ChildminderUrgentRegisterDto;
import io.wisoft.poomi.bind.request.childminder.urgent.ChildminderUrgentModifiedRequest;
import io.wisoft.poomi.bind.request.childminder.urgent.ChildminderUrgentRegisterRequest;
import io.wisoft.poomi.bind.utils.LocalDateTimeUtils;
import io.wisoft.poomi.domain.childminder.urgent.ChildminderUrgent;
import io.wisoft.poomi.domain.childminder.urgent.ChildminderUrgentRepository;
import io.wisoft.poomi.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChildminderUrgentService {

    private final ChildminderUrgentRepository childminderUrgentRepository;

    @Transactional(readOnly = true)
    public List<ChildminderUrgentLookupDto> lookupAllChildminderUrgent(final Member member) {
        List<ChildminderUrgent> childminderUrgentList = childminderUrgentRepository
                .findAllByAddressTag(member.getAddressTag());
        return childminderUrgentList.stream()
                .map(ChildminderUrgentLookupDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChildminderUrgentRegisterDto registerChildminderUrgent(
            final ChildminderUrgentRegisterRequest childminderUrgentRegisterRequest,
            final Member member) {
        checkChildminderActivityTime(
                childminderUrgentRegisterRequest.getStartTime(), childminderUrgentRegisterRequest.getEndTime()
        );

        ChildminderUrgent childminderUrgent = ChildminderUrgent.of(childminderUrgentRegisterRequest, member);
        log.info("Generate childminder urgent entity");

        childminderUrgentRepository.save(childminderUrgent);
        log.info("Save childminder urgent id: {}", childminderUrgent.getId());


        return ChildminderUrgentRegisterDto.of(childminderUrgent);
    }

    @Transactional
    public ChildminderUrgentModifiedDto modifiedChildminderUrgent(
            final Long urgentId,
            final ChildminderUrgentModifiedRequest childminderUrgentModifiedRequest,
            final Member member) {

        checkChildminderActivityTime(
                childminderUrgentModifiedRequest.getStartTime(), childminderUrgentModifiedRequest.getEndTime()
        );

        ChildminderUrgent childminderUrgent = generateChildminderUrgentById(urgentId);

        verifyPermission(childminderUrgent, member);

        modifyChildminderUrgent(childminderUrgent, childminderUrgentModifiedRequest);

        return ChildminderUrgentModifiedDto.of(childminderUrgent);
    }

    private ChildminderUrgent generateChildminderUrgentById(final Long urgentId) {
        ChildminderUrgent childminderUrgent = childminderUrgentRepository.getById(urgentId);
        log.info("Generate childminder urgent id: {}", urgentId);

        return childminderUrgent;
    }

    private void checkChildminderActivityTime(final LocalDateTime startTime, final LocalDateTime endTime) {
        LocalDateTimeUtils
                .checkChildminderActivityTime(
                        startTime,
                        endTime
                );
        log.info("Check childminder activity time through request");
    }

    private void verifyPermission(final ChildminderUrgent childminderUrgent, final Member member) {
        childminderUrgent.verifyPermission(member);
        log.info("Verify permission of childminder urgent id: {}", childminderUrgent.getId());
    }

    private void modifyChildminderUrgent(final ChildminderUrgent childminderUrgent,
                                         final ChildminderUrgentModifiedRequest childminderUrgentModifiedRequest) {
        childminderUrgent.modifiedFor(childminderUrgentModifiedRequest);
        childminderUrgentRepository.save(childminderUrgent);
        log.info("Update childminder urgent entity id: {}", childminderUrgent.getId());
    }

}
