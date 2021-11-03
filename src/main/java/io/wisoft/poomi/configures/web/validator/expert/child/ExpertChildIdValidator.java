package io.wisoft.poomi.configures.web.validator.expert.child;

import io.wisoft.poomi.domain.child_care.expert.RecruitType;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExpertChildIdValidator implements ConstraintValidator<ExpertChildId, Object> {

    private String recruitType;
    private String childId;

    @Override
    public void initialize(ExpertChildId constraintAnnotation) {
        this.recruitType = constraintAnnotation.recruitType();
        this.childId = constraintAnnotation.childId();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        final BeanWrapperImpl requestInstance = new BeanWrapperImpl(value);

        RecruitType recruitType = (RecruitType) requestInstance
                .getPropertyValue(this.recruitType);
        Long childId = (Long) requestInstance
                .getPropertyValue(this.childId);

        if (recruitType.equals(RecruitType.RECRUIT)) {
            return childId != null;
        } else {
            return true;
        }
    }
}
