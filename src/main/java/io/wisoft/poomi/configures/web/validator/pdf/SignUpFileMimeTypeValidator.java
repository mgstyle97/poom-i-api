package io.wisoft.poomi.configures.web.validator.pdf;

import io.wisoft.poomi.global.utils.UploadFileUtils;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class SignUpFileMimeTypeValidator implements ConstraintValidator<SignUpFile, List<String>> {

    @Override
    public boolean isValid(List<String> fileDataList, ConstraintValidatorContext context) {
        return fileDataList.stream()
                .map(UploadFileUtils::getContentType)
                .allMatch(this::checkImageOrPDF);
    }

    private boolean checkImageOrPDF(final String contentType) {
        boolean isImageType = contentType.startsWith("image");
        boolean isPDFType = contentType.equals("application/pdf");
        return isImageType || isPDFType;
    }
}
