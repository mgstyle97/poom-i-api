package io.wisoft.poomi.service.childminder.urgent;

import io.wisoft.poomi.global.dto.response.childminder.urgent.ChildminderUrgentLookupResponse;
import io.wisoft.poomi.global.dto.response.childminder.urgent.ChildminderUrgentModifiedResponse;
import io.wisoft.poomi.global.dto.response.childminder.urgent.ChildminderUrgentRegisterResponse;
import io.wisoft.poomi.global.dto.request.childminder.urgent.ChildminderUrgentModifiedRequest;
import io.wisoft.poomi.global.dto.request.childminder.urgent.ChildminderUrgentRegisterRequest;
import io.wisoft.poomi.global.utils.LocalDateTimeUtils;
import io.wisoft.poomi.domain.childminder.urgent.ChildminderUrgent;
import io.wisoft.poomi.domain.childminder.urgent.ChildminderUrgentRepository;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.childminder.ContentPermissionVerifier;
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
    public List<ChildminderUrgentLookupResponse> lookupAllChildminderUrgent(final Member member) {
        List<ChildminderUrgent> childminderUrgentList = childminderUrgentRepository
                .findAllByAddressTag(member.getAddressTag());
        return childminderUrgentList.stream()
                .map(ChildminderUrgentLookupResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChildminderUrgentRegisterResponse registerChildminderUrgent(
            final ChildminderUrgentRegisterRequest childminderUrgentRegisterRequest,
            final Member member) {
        checkChildminderActivityTime(
                childminderUrgentRegisterRequest.getStartTime(), childminderUrgentRegisterRequest.getEndTime()
        );

        ChildminderUrgent childminderUrgent = ChildminderUrgent.of(childminderUrgentRegisterRequest, member);
        log.info("Generate childminder urgent entity");

        childminderUrgentRepository.save(childminderUrgent);
        log.info("Save childminder urgent id: {}", childminderUrgent.getId());


        return ChildminderUrgentRegisterResponse.of(childminderUrgent);
    }

    @Transactional
    public ChildminderUrgentModifiedResponse modifiedChildminderUrgent(
            final Long urgentId,
            final ChildminderUrgentModifiedRequest childminderUrgentModifiedRequest,
            final Member member) {

        checkChildminderActivityTime(
                childminderUrgentModifiedRequest.getStartTime(), childminderUrgentModifiedRequest.getEndTime()
        );

        ChildminderUrgent childminderUrgent = generateChildminderUrgentById(urgentId);

        ContentPermissionVerifier.verifyPermission(childminderUrgent.getWriter(), member);

        modifyChildminderUrgent(childminderUrgent, childminderUrgentModifiedRequest);

        return ChildminderUrgentModifiedResponse.of(childminderUrgent);
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

    private void modifyChildminderUrgent(final ChildminderUrgent childminderUrgent,
                                         final ChildminderUrgentModifiedRequest childminderUrgentModifiedRequest) {
        childminderUrgent.modifiedFor(childminderUrgentModifiedRequest);
        childminderUrgentRepository.save(childminderUrgent);
        log.info("Update childminder urgent entity id: {}", childminderUrgent.getId());
    }

}
