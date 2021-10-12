package io.wisoft.poomi.configures.web.validator.pdf;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SignUpFileMimeTypeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SignUpFile {

    String message() default "이미지 혹은 PDF 파일이 아닙니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
