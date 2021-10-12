package io.wisoft.poomi.configures.web.validator.pdf;

import io.wisoft.poomi.global.utils.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class SignUpFileMimeTypeValidator implements ConstraintValidator<SignUpFile, List<MultipartFile>> {

    @Override
    public boolean isValid(List<MultipartFile> needToVerifyFiles, ConstraintValidatorContext context) {
        return needToVerifyFiles.stream()
                .map(FileUtils::getMimeType)
                .allMatch(this::checkImageOrPDF);
    }

    private boolean checkImageOrPDF(final String mimeType) {
        boolean isImageType = mimeType.startsWith("image");
        boolean isPDFType = mimeType.equals("application/pdf");
        return isImageType || isPDFType;
    }
}
