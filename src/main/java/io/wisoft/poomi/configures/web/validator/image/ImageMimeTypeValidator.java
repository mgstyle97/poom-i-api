package io.wisoft.poomi.configures.web.validator.image;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageMimeTypeValidator implements ConstraintValidator<Image, String> {

    @Override
    public boolean isValid(String fileMetaData, ConstraintValidatorContext context) {

        return isMimeTypeIncludingImage(fileMetaData);
    }

    private boolean isMimeTypeIncludingImage(final String fileMetaData) {
        final int mimeTypeStartIndex = fileMetaData.indexOf(":") + 1;
        return fileMetaData.substring(mimeTypeStartIndex).startsWith("image/");
    }
}
