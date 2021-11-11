package io.wisoft.poomi.configures.web.validator.member;

import io.wisoft.poomi.global.dto.request.member.ChildAddRequest;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class SignupChildValidator implements ConstraintValidator<SignupChild, Object> {

    private String children;
    private String fileData;

    @Override
    public void initialize(SignupChild constraintAnnotation) {
        this.children = constraintAnnotation.children();
        this.fileData = constraintAnnotation.familyCertificationFileData();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        final BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);

        List<ChildAddRequest> children = (List<ChildAddRequest>) beanWrapper.getPropertyValue(this.children);

        String fileData = (String) beanWrapper.getPropertyValue(this.fileData);

        if (!CollectionUtils.isEmpty(children)) {
            return fileData != null;
        }

        return true;
    }

}
