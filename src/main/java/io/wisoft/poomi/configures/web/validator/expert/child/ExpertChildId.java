package io.wisoft.poomi.configures.web.validator.expert.child;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ExpertChildIdValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpertChildId {

    String message ();

    String recruitType ();

    String childId ();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
