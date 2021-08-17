package io.wisoft.poomi.bind.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class FileUtils {

    private static final String USER_INFO_DOCUMENT_PATH = System.getProperty("user.dir") + "/img/";

    public static void saveImageWithUserEmail(String email, List<MultipartFile> images) {
        int idx = 1;

        try {
            for (MultipartFile image : images) {
                String extension = FilenameUtils.getExtension(image.getOriginalFilename());
                File destImage =
                        new File(
                                USER_INFO_DOCUMENT_PATH + email + "/" + email + "_" + idx + "." + extension
                        );
                if (!destImage.exists()) {
                    destImage.mkdirs();
                }
                image.transferTo(destImage);
                log.info("Save file: {}", destImage.getName());
                idx++;
            }
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

}
