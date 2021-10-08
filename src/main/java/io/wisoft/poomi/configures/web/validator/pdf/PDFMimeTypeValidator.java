package io.wisoft.poomi.configures.web.validator.pdf;

import io.wisoft.poomi.global.utils.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class PDFMimeTypeValidator implements ConstraintValidator<PDF, List<MultipartFile>> {

    @Override
    public boolean isValid(List<MultipartFile> needToVerifyFiles, ConstraintValidatorContext context) {
        return needToVerifyFiles.stream()
                .map(FileUtils::getMimeType)
                .allMatch(mimeType -> mimeType.equals("application/pdf"));
    }
}
