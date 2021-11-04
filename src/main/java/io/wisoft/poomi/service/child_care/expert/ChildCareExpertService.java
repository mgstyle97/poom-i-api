package io.wisoft.poomi.service.child_care.expert;

import io.wisoft.poomi.domain.child_care.expert.RecruitType;
import io.wisoft.poomi.domain.child_care.expert.apply.ChildCareExpertApply;
import io.wisoft.poomi.domain.child_care.expert.apply.ChildCareExpertApplyRepository;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.domain.member.child.ChildRepository;
import io.wisoft.poomi.global.aop.child_care.NoAccessCheck;
import io.wisoft.poomi.global.dto.request.child_care.expert.ChildCareExpertApplyModifiedRequest;
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
import io.wisoft.poomi.service.member.ChildService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    private final ChildService childService;

    @NoAccessCheck
    @Transactional(readOnly = true)
    public List<ChildCareExpertLookupResponse> lookupAllChildCareExpert(final Member member) {
        List<ChildCareExpert> childCareExpertList = childCareExpertRepository
                .findAllByAddressTag(member.getAddressTag());
        return childCareExpertList.stream()
                .map(expert -> ChildCareExpertLookupResponse.of(expert, member))
                .collect(Collectors.toList());
    }

    @NoAccessCheck
    @Transactional
    public ChildCareExpertRegisterResponse registerChildCareExpert(
            final ChildCareExpertRegisterRequest childCareExpertRegisterRequest,
            final Member member) {

        Child child = childService
                .checkChildId(member, Optional.ofNullable(childCareExpertRegisterRequest.getChildId()));

        ChildCareExpert childCareExpert = ChildCareExpert.of(childCareExpertRegisterRequest, member, child);
        log.info("Generate child care expert entity");

        childCareExpertRepository.save(childCareExpert);
        log.info("Save child care expert id: {}", childCareExpert.getId());

        return ChildCareExpertRegisterResponse.of(childCareExpert);
    }

    @Transactional(readOnly = true)
    public ChildCareExpertLookupResponse lookupChildCareExpert(final Long expertId, final Member member) {
        ChildCareExpert childCareExpert = generateChildCareExpertById(expertId);

        return ChildCareExpertLookupResponse.of(childCareExpert, member);
    }

    @Transactional
    public ChildCareExpertModifiedResponse modifiedChildCareExpert(
            final Long expertId,
            final ChildCareExpertModifiedRequest childCareExpertModifiedRequest,
            final Member member) {

        ChildCareExpert childCareExpert = generateChildCareExpertById(expertId);

        Child child = childService
                .checkChildId(member, Optional.ofNullable(childCareExpertModifiedRequest.getChildId()));
        modifyChildCareExpert(childCareExpert, childCareExpertModifiedRequest, child);

        return ChildCareExpertModifiedResponse.of(childCareExpert);
    }

    @Transactional
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

        ChildCareExpertApply expertApply = generateExpertApply(childCareExpert, childCareExpertApplyRequest, member);
        log.info("Generate child care expert apply through request");

        childCareExpertApplyRepository.save(expertApply);
        log.info("Save child care expert apply id: {}", expertApply.getId());

        member.addExpertApply(expertApply);
        log.info("Add apply to member entity and update member");

        childCareExpert.addApply(expertApply);
        log.info("Add application to expert entity and update child care expert");
    }

    @Transactional(readOnly = true)
    public List<ChildCareExpertApplyLookupResponse> lookupAllApplicationChildCareExpert(
            final Long expertId, final Member member) {
        ChildCareExpert childCareExpert = generateChildCareExpertById(expertId);

        return childCareExpert.getApplications().stream()
                .map(ChildCareExpertApplyLookupResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void modifiedChildCareExpertApply(final Long expertId, final Long applyId,
                                             final Member member,
                                             final ChildCareExpertApplyModifiedRequest applyModifiedRequest) {
        ChildCareExpert childCareExpert = generateChildCareExpertById(expertId);
        ChildCareExpertApply expertApply = checkApplyIncludedInExpert(childCareExpert, applyId);

        validateWriterOfApply(expertApply, member);

        Child child = validateChildId(childCareExpert, applyModifiedRequest.getChildId(), member);
        expertApply.modifiedByRequest(applyModifiedRequest.getContents(), child);
        childCareExpertApplyRepository.save(expertApply);
    }

    @Transactional
    public void removeChildCareExpertApply(final Long expertId, final Long applyId,
                                           final Member member) {
        ChildCareExpert childCareExpert = generateChildCareExpertById(expertId);
        ChildCareExpertApply expertApply = checkApplyIncludedInExpert(childCareExpert, applyId);

        validateWriterOfApply(expertApply, member);

        deleteChildCareExpertApply(childCareExpert, expertApply);
    }

    @Transactional
    public void likeChildCareExpert(final Long expertId, final Member member) {
        ChildCareExpert childCareExpert = generateChildCareExpertById(expertId);

        childCareExpert.addLikes(member);
    }

    @Transactional
    public void removeLikeChildCareExpert(final Long expertId, final Member member) {
        ChildCareExpert expert = generateChildCareExpertById(expertId);

        expert.removeLike(member);
    }

    @Transactional
    public void approveExpertApply(final Long expertId, final Long applyId, final Member member) {
        ChildCareExpert childCareExpert = generateChildCareExpertById(expertId);
        childCareExpert.isNotWriter(member);
        log.info("Check expert writer");

        ChildCareExpertApply expertApply = childCareExpertApplyRepository.getById(applyId);
        childCareExpert.checkApplyIncluded(expertApply);

        childCareExpert.approveApply(expertApply);
        childCareExpertApplyRepository.delete(expertApply);
    }

    private ChildCareExpert generateChildCareExpertById(final Long expertId) {
        ChildCareExpert childCareExpert = childCareExpertRepository.getById(expertId);
        log.info("Generate child care expert id: {}", expertId);

        return childCareExpert;
    }

    private void modifyChildCareExpert(final ChildCareExpert childCareExpert,
                                       final ChildCareExpertModifiedRequest childCareExpertModifiedRequest,
                                       final Child child) {
        childCareExpert.modifiedFor(childCareExpertModifiedRequest, child);
        childRepository.save(child);
        childCareExpertRepository.save(childCareExpert);
        log.info("Update child care expert entity id: {}", childCareExpert.getId());
    }

    private void deleteChildCareExpert(final ChildCareExpert childCareExpert,
                                       final Member member) {
        childCareExpert.resetAssociated();

        childCareExpertApplyRepository.deleteAll(childCareExpert.getApplications());
        childCareExpertRepository.delete(childCareExpert);
        log.info("Delete child care expert content id: {}", childCareExpert.getId());
    }

    private ChildCareExpertApply generateExpertApply(final ChildCareExpert childCareExpert,
                                                     final ChildCareExpertApplyRequest applyRequest,
                                                     final Member member) {
        Child child = validateChildId(childCareExpert, applyRequest.getChildId(), member);

        return ChildCareExpertApply.of(applyRequest, childCareExpert, member, child);
    }

    private Child validateChildId(final ChildCareExpert childCareExpert,
                                  final Long childId,
                                  final Member member) {
        if (childCareExpert.getRecruitType().equals(RecruitType.VOLUNTEER) && childId == null) {
            throw new IllegalArgumentException("품앗이꾼에게 도움을 요청할 때는 자식 정보를 제공해야 합니다.");
        }

        return childService.checkChildId(member, Optional.ofNullable(childId));
    }

    private ChildCareExpertApply checkApplyIncludedInExpert(final ChildCareExpert childCareExpert, final Long applyId) {
        ChildCareExpertApply expertApply = childCareExpertApplyRepository.getById(applyId);
        childCareExpert.checkApplyIncluded(expertApply);
        log.info("Check apply is included in expert");

        return expertApply;
    }

    private void validateWriterOfApply(final ChildCareExpertApply expertApply, final Member member) {
        expertApply.checkWriter(member);
        log.info("Check apply writer");
    }


    private void deleteChildCareExpertApply(final ChildCareExpert childCareExpert,
                                            final ChildCareExpertApply expertApply) {
        childCareExpert.removeApply(expertApply);
        expertApply.reset();
        childCareExpertApplyRepository.delete(expertApply);
        log.info("Delete child care expert apply id: {}", expertApply.getId());
    }

}
