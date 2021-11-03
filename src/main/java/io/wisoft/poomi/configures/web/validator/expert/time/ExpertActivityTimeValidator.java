package io.wisoft.poomi.configures.web.validator.expert.time;

import io.wisoft.poomi.global.utils.LocalDateTimeUtils;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class ExpertActivityTimeValidator implements ConstraintValidator<ExpertActivityTime, Object> {

    private String startTime;
    private String endTime;



    @Override
    public void initialize(ExpertActivityTime constraintAnnotation) {
        this.startTime = constraintAnnotation.startTime();
        this.endTime = constraintAnnotation.endTime();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        final BeanWrapperImpl requestInstance = new BeanWrapperImpl(value);

        LocalDateTime startTime = (LocalDateTime) requestInstance.getPropertyValue(this.startTime);
        LocalDateTime endTime = (LocalDateTime) requestInstance.getPropertyValue(this.endTime);

        return LocalDateTimeUtils.checkChildCareContentActivityTime(startTime, endTime);
    }
}
