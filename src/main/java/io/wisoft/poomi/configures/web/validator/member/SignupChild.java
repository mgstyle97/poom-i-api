package io.wisoft.poomi.configures.web.validator.member;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SignupChildValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SignupChild {

    String message ();

    String children ();

    String familyCertificationFileData ();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
