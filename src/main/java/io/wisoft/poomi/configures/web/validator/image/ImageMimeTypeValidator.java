package io.wisoft.poomi.configures.web.validator.image;

import io.wisoft.poomi.global.utils.MultipartFileUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ImageMimeTypeValidator implements ConstraintValidator<Image, List<MultipartFile>> {

    @Override
    public boolean isValid(List<MultipartFile> needToVerifyFiles, ConstraintValidatorContext context) {

        return needToVerifyFiles.stream()
                .map(MultipartFileUtils::getMimeType)
                .allMatch(mimeType -> mimeType.startsWith("image"));
    }
}
