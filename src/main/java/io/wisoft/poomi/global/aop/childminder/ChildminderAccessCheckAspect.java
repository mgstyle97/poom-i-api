package io.wisoft.poomi.global.aop.childminder;

import io.wisoft.poomi.domain.childminder.BaseChildminderEntity;
import io.wisoft.poomi.domain.childminder.classes.ChildminderClass;
import io.wisoft.poomi.domain.childminder.classes.ChildminderClassRepository;
import io.wisoft.poomi.domain.childminder.urgent.ChildminderUrgent;
import io.wisoft.poomi.domain.childminder.urgent.ChildminderUrgentRepository;
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

    private final ChildminderClassRepository childminderClassRepository;
    private final ChildminderUrgentRepository childminderUrgentRepository;

    @Before("execution(public * io.wisoft.poomi.service.childminder.classes.*.*(..)) " +
            "&& !@target(io.wisoft.poomi.global.aop.childminder.NoAccessCheck) " +
            "&& !@annotation(io.wisoft.poomi.global.aop.childminder.NoAccessCheck)")
    public void childminderClassAccessCheck(final JoinPoint joinPoint) {
        Long classId = setContentId(joinPoint);
        Member member = setMember(joinPoint);

        ChildminderClass childminderClass = childminderClassRepository.getById(classId);

        childminderAccessCheck(childminderClass, member.getAddressTag());
    }

    @Before("execution(public * io.wisoft.poomi.service.childminder.urgent.*.*(..)) " +
            "&& !@target(io.wisoft.poomi.global.aop.childminder.NoAccessCheck) " +
            "&& !@annotation(io.wisoft.poomi.global.aop.childminder.NoAccessCheck)")
    public void childminderUrgentAccessCheck(final JoinPoint joinPoint) {
        Long urgentId = setContentId(joinPoint);
        Member member = setMember(joinPoint);

        ChildminderUrgent childminderUrgent = childminderUrgentRepository.getById(urgentId);

        childminderAccessCheck(childminderUrgent, member.getAddressTag());
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

    private void childminderAccessCheck(final BaseChildminderEntity childminderEntity, final AddressTag addressTag) {
        childminderEntity.checkAccessPermission(addressTag);
        log.info("Check access permission of childminder content");
    }

}
