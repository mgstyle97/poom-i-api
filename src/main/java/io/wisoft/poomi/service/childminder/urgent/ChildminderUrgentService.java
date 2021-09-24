package io.wisoft.poomi.service.childminder.urgent;

import io.wisoft.poomi.domain.childminder.urgent.application.ChildminderUrgentApplication;
import io.wisoft.poomi.domain.childminder.urgent.application.ChildminderUrgentApplicationRepository;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.domain.member.child.ChildRepository;
import io.wisoft.poomi.global.aop.childminder.NoAccessCheck;
import io.wisoft.poomi.global.dto.request.childminder.urgent.ChildminderUrgentApplyRequest;
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
    private final ChildminderUrgentApplicationRepository childminderUrgentApplicationRepository;
    private final ChildRepository childRepository;

    @NoAccessCheck
    @Transactional(readOnly = true)
    public List<ChildminderUrgentLookupResponse> lookupAllChildminderUrgent(final AddressTag addressTag) {
        List<ChildminderUrgent> childminderUrgentList = childminderUrgentRepository
                .findAllByAddressTag(addressTag);
        return childminderUrgentList.stream()
                .map(ChildminderUrgentLookupResponse::of)
                .collect(Collectors.toList());
    }

    @NoAccessCheck
    @Transactional
    public ChildminderUrgentRegisterResponse registerChildminderUrgent(
            final ChildminderUrgentRegisterRequest childminderUrgentRegisterRequest,
            final Member member) {
        checkChildminderActivityTime(
                childminderUrgentRegisterRequest.getStartTime(),
                childminderUrgentRegisterRequest.getEndTime()
        );

        Child child = checkChildId(member, childminderUrgentRegisterRequest);

        ChildminderUrgent childminderUrgent = ChildminderUrgent.of(childminderUrgentRegisterRequest, member, child);
        log.info("Generate childminder urgent entity");

        childminderUrgentRepository.save(childminderUrgent);
        log.info("Save childminder urgent id: {}", childminderUrgent.getId());

        return ChildminderUrgentRegisterResponse.of(childminderUrgent);
    }

    @Transactional(readOnly = true)
    public ChildminderUrgentLookupResponse lookupChildminderUrgent(final Long urgentId, final Member member) {
        ChildminderUrgent childminderUrgent = generateChildminderUrgentById(urgentId);

        return ChildminderUrgentLookupResponse.of(childminderUrgent);
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

        modifyChildminderUrgent(childminderUrgent, childminderUrgentModifiedRequest);

        return ChildminderUrgentModifiedResponse.of(childminderUrgent);
    }

    @Transactional
    public void applyChildminderUrgent(final Long urgentId,
                                       final Member member,
                                       final ChildminderUrgentApplyRequest childminderUrgentApplyRequest) {
        ChildminderUrgent childminderUrgent = generateChildminderUrgentById(urgentId);

        childminderUrgent.isWriter(member);

        ChildminderUrgentApplication application =
                ChildminderUrgentApplication.of(childminderUrgentApplyRequest, childminderUrgent, member);
        log.info("Generate childminder urgent application through request");

        childminderUrgentApplicationRepository.save(application);
        log.info("Save childminder urgent application id: {}", application.getId());

        childminderUrgent.addApplication(application);
        childminderUrgentRepository.save(childminderUrgent);
        log.info("Add application to urgent entity and update childminder urgent");
    }

    @Transactional
    public void likeChildminderUrgent(final Long urgentId, final Member member) {

    }

    private Child checkChildId(
            final Member member,
            final ChildminderUrgentRegisterRequest childminderUrgentRegisterRequest) {
        if (!childminderUrgentRegisterRequest.getIsRecruit()) {
            return null;
        }

        Child child = childRepository.getById(childminderUrgentRegisterRequest.getChildId());
        member.checkChildInChildren(child);

        return child;
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
