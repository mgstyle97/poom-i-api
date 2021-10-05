package io.wisoft.poomi.global.aop.childminder;

import io.wisoft.poomi.domain.child_care.BaseChildCareEntity;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroupRepository;
import io.wisoft.poomi.domain.child_care.expert.ChildCareExpert;
import io.wisoft.poomi.domain.child_care.expert.ChildCareExpertRepository;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.AddressTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Aspect
public class ChildminderAccessCheckAspect {

    private final ChildCareGroupRepository childCareGroupRepository;
    private final ChildCareExpertRepository childCareExpertRepository;

    @Before("execution(public * io.wisoft.poomi.service.child_care.group.*.*(..)) " +
            "&& !@target(io.wisoft.poomi.global.aop.childminder.NoAccessCheck) " +
            "&& !@annotation(io.wisoft.poomi.global.aop.childminder.NoAccessCheck)")
    public void childminderClassAccessCheck(final JoinPoint joinPoint) {
        Long classId = setContentId(joinPoint);
        Member member = setMember(joinPoint);

        ChildCareGroup childCareGroup = childCareGroupRepository.getById(classId);

        childminderAccessCheck(childCareGroup, member.getAddressTag());
    }

    @Before("execution(public * io.wisoft.poomi.service.child_care.expert.*.*(..)) " +
            "&& !@target(io.wisoft.poomi.global.aop.childminder.NoAccessCheck) " +
            "&& !@annotation(io.wisoft.poomi.global.aop.childminder.NoAccessCheck)")
    public void childminderUrgentAccessCheck(final JoinPoint joinPoint) {
        Long urgentId = setContentId(joinPoint);
        Member member = setMember(joinPoint);

        ChildCareExpert childCareExpert = childCareExpertRepository.getById(urgentId);

        childminderAccessCheck(childCareExpert, member.getAddressTag());
    }
    private Long setContentId(final JoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof Long) {
                return (Long) arg;
            }
        }

        return null;
    }

    private Member setMember(final JoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof Member) {
                return (Member) arg;
            }
        }

        return null;
    }

    private void childminderAccessCheck(final BaseChildCareEntity childminderEntity, final AddressTag addressTag) {
        childminderEntity.checkAccessPermission(addressTag);
        log.info("Check access permission of childminder content");
    }

}
