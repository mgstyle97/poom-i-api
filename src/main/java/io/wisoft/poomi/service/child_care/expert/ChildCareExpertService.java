package io.wisoft.poomi.service.child_care.expert;

import io.wisoft.poomi.domain.child_care.expert.RecruitType;
import io.wisoft.poomi.domain.child_care.expert.apply.ChildCareExpertApply;
import io.wisoft.poomi.domain.child_care.expert.apply.ChildCareExpertApplyRepository;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.domain.member.child.ChildRepository;
import io.wisoft.poomi.global.aop.childminder.NoAccessCheck;
import io.wisoft.poomi.global.dto.request.child_care.expert.ChildCareExpertApplyRequest;
import io.wisoft.poomi.global.dto.response.child_care.expert.ChildCareExpertApplyLookupResponse;
import io.wisoft.poomi.global.dto.response.child_care.expert.ChildCareExpertLookupResponse;
import io.wisoft.poomi.global.dto.response.child_care.expert.ChildCareExpertModifiedResponse;
import io.wisoft.poomi.global.dto.response.child_care.expert.ChildCareExpertRegisterResponse;
import io.wisoft.poomi.global.dto.request.child_care.expert.ChildCareExpertModifiedRequest;
import io.wisoft.poomi.global.dto.request.child_care.expert.ChildCareExpertRegisterRequest;
import io.wisoft.poomi.global.utils.LocalDateTimeUtils;
import io.wisoft.poomi.domain.child_care.expert.ChildCareExpert;
import io.wisoft.poomi.domain.child_care.expert.ChildCareExpertRepository;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.child_care.ContentPermissionVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChildCareExpertService {

    private final MemberRepository memberRepository;
    private final ChildCareExpertRepository childCareExpertRepository;
    private final ChildCareExpertApplyRepository childCareExpertApplyRepository;
    private final ChildRepository childRepository;

    @NoAccessCheck
    @Transactional(readOnly = true)
    public List<ChildCareExpertLookupResponse> lookupAllChildCareExpert(final AddressTag addressTag) {
        List<ChildCareExpert> childCareExpertList = childCareExpertRepository
                .findAllByAddressTag(addressTag);
        return childCareExpertList.stream()
                .map(ChildCareExpertLookupResponse::of)
                .collect(Collectors.toList());
    }

    @NoAccessCheck
    @Transactional
    public ChildCareExpertRegisterResponse registerChildCareExpert(
            final ChildCareExpertRegisterRequest childCareExpertRegisterRequest,
            final Member member) {
        checkExpertActivityTime(
                childCareExpertRegisterRequest.getStartTime(),
                childCareExpertRegisterRequest.getEndTime()
        );

        Child child = checkChildId(member, childCareExpertRegisterRequest);

        ChildCareExpert childCareExpert = ChildCareExpert.of(childCareExpertRegisterRequest, member, child);
        log.info("Generate child care expert entity");

        childCareExpertRepository.save(childCareExpert);
        log.info("Save child care expert id: {}", childCareExpert.getId());

        return ChildCareExpertRegisterResponse.of(childCareExpert);
    }

    @Transactional(readOnly = true)
    public ChildCareExpertLookupResponse lookupChildCareExpert(final Long expertId, final Member member) {
        ChildCareExpert childCareExpert = generateChildCareExpertById(expertId);

        return ChildCareExpertLookupResponse.of(childCareExpert);
    }

    @Transactional
    public ChildCareExpertModifiedResponse modifiedChildCareExpert(
            final Long expertId,
            final ChildCareExpertModifiedRequest childCareExpertModifiedRequest,
            final Member member) {

        checkExpertActivityTime(
                childCareExpertModifiedRequest.getStartTime(), childCareExpertModifiedRequest.getEndTime()
        );

        ChildCareExpert childCareExpert = generateChildCareExpertById(expertId);

        modifyChildCareExpert(childCareExpert, childCareExpertModifiedRequest);

        return ChildCareExpertModifiedResponse.of(childCareExpert);
    }

    public void removeChildCareExpert(final Long expertId, final Member member) {
        ChildCareExpert childCareExpert = generateChildCareExpertById(expertId);

        ContentPermissionVerifier.verifyModifyPermission(childCareExpert.getWriter(), member);

        deleteChildCareExpert(childCareExpert, member);
    }

    @Transactional
    public void applyChildCareExpert(final Long expertId,
                                     final Member member,
                                     final ChildCareExpertApplyRequest childCareExpertApplyRequest) {
        ChildCareExpert childCareExpert = generateChildCareExpertById(expertId);

        childCareExpert.isWriter(member);
        childCareExpert.isAlreadyApplier(member);
        log.info("Check member to make a request id: {}", member.getId());

        ChildCareExpertApply application =
                ChildCareExpertApply.of(childCareExpertApplyRequest, childCareExpert, member);
        log.info("Generate child care expert application through request");

        childCareExpertApplyRepository.save(application);
        log.info("Save child care expert application id: {}", application.getId());

        member.addApplication(application);
        memberRepository.save(member);
        log.info("Add application to member entity and update member");

        childCareExpert.addApplication(application);
        childCareExpertRepository.save(childCareExpert);
        log.info("Add application to urgent entity and update child care expert");
    }

    @Transactional(readOnly = true)
    public List<ChildCareExpertApplyLookupResponse> lookupAllApplicationChildCareExpert(
            final Long expertId, final Member member) {
        ChildCareExpert childCareExpert = generateChildCareExpertById(expertId);

        Set<ChildCareExpertApply> applications = childCareExpert.getApplications();

        return applications.stream()
                .map(ChildCareExpertApplyLookupResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void likeChildCareExpert(final Long expertId, final Member member) {
        ChildCareExpert childCareExpert = generateChildCareExpertById(expertId);

        childCareExpert.addLikes(member);
        memberRepository.save(member);
    }

    private Child checkChildId(
            final Member member,
            final ChildCareExpertRegisterRequest childCareExpertRegisterRequest) {
        if (childCareExpertRegisterRequest.getRecruitType().equals(RecruitType.VOLUNTEER)) {
            return null;
        }

        Child child = childRepository.getById(childCareExpertRegisterRequest.getChildId());
        member.checkChildInChildren(child);

        return child;
    }

    private ChildCareExpert generateChildCareExpertById(final Long expertId) {
        ChildCareExpert childCareExpert = childCareExpertRepository.getById(expertId);
        log.info("Generate child care expert id: {}", expertId);

        return childCareExpert;
    }

    private void checkExpertActivityTime(final LocalDateTime startTime, final LocalDateTime endTime) {
        LocalDateTimeUtils
                .checkChildCareContentActivityTime(
                        startTime,
                        endTime
                );
        log.info("Check child care expert activity time through request");
    }

    private void modifyChildCareExpert(final ChildCareExpert childCareExpert,
                                       final ChildCareExpertModifiedRequest childCareExpertModifiedRequest) {
        childCareExpert.modifiedFor(childCareExpertModifiedRequest);
        childCareExpertRepository.save(childCareExpert);
        log.info("Update child care expert entity id: {}", childCareExpert.getId());
    }

    private void deleteChildCareExpert(final ChildCareExpert childCareExpert,
                                       final Member member) {
        childCareExpert.resetAssociated();
        member.removeWrittenExpert(childCareExpert);
        childCareExpertApplyRepository.deleteAll(childCareExpert.getApplications());
        childCareExpertRepository.delete(childCareExpert);
    }

}
