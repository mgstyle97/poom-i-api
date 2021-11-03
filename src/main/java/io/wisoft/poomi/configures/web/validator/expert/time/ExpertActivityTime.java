package io.wisoft.poomi.configures.web.validator.expert.time;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ExpertActivityTimeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpertActivityTime {

    String message ();

    String startTime ();

    String endTime ();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
