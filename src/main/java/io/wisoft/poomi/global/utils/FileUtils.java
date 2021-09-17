package io.wisoft.poomi.global.utils;

import io.wisoft.poomi.domain.childminder.classes.ChildminderClass;
import io.wisoft.poomi.domain.childminder.classes.image.Image;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class FileUtils {

    private static final String IMAGE_SAVE_PATH = System.getProperty("user.dir") + "/img/";

    public static void saveImageWithUserEmail(String email, List<MultipartFile> images) {
        int idx = 1;

        if (images == null) {
            throw new IllegalArgumentException("회원의 신분을 입증할 파일이 없습니다.");
        }

        try {
            for (MultipartFile image : images) {
                String extension = FilenameUtils.getExtension(image.getOriginalFilename());
                File destImage =
                        new File(
                                IMAGE_SAVE_PATH + email + "/" + email + "_" + idx + "." + extension
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

    public static Set<Image> saveImageWithClassId(final ChildminderClass childminderClass,
                                                  final List<MultipartFile> images,
                                                  final String domainInfo) {
        int idx = 1;

        Set<Image> imageEntities = new HashSet<>();
        try {
            for (MultipartFile image : images) {
                String extension = FilenameUtils.getExtension(image.getOriginalFilename());
                Long classId = childminderClass.getId();
                File destImage =
                    new File(
                        IMAGE_SAVE_PATH + classId + "/" + classId + "_" + idx + "." + extension
                    );

                if (!destImage.exists()) {
                    destImage.mkdirs();
                }
                image.transferTo(destImage);
                log.info("Save file: {}", destImage.getName());
                idx++;

                imageEntities.add(
                    Image.of(destImage, childminderClass, image.getOriginalFilename(), domainInfo)
                );
            }
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        return imageEntities;
    }

}
