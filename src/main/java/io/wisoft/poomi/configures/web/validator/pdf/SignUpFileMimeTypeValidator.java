package io.wisoft.poomi.configures.web.validator.pdf;

import io.wisoft.poomi.global.utils.UploadFileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@RequiredArgsConstructor
public class SignUpFileMimeTypeValidator implements ConstraintValidator<SignUpFile, List<String>> {

    private final UploadFileUtils uploadFileUtils;

    @Override
    public boolean isValid(List<String> fileDataList, ConstraintValidatorContext context) {
        return fileDataList.stream()
                .map(uploadFileUtils::getFileExtension)
                .allMatch(this::checkImageOrPDF);
    }

    private boolean checkImageOrPDF(final String extension) {
        boolean isImageType = extension.equals("jpeg") || extension.equals("png");
        boolean isPDFType = extension.equals("pdf");
        return isImageType || isPDFType;
    }
}
