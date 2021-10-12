package io.wisoft.poomi.global.utils;

import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.board.GroupBoard;
import io.wisoft.poomi.domain.child_care.group.image.Image;
import io.wisoft.poomi.global.exception.exceptions.FileNotReadableException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class FileUtils {

    private static final String IMAGE_SAVE_PATH = System.getProperty("user.dir") + "/img/";
    private static Tika tika;

    @PostConstruct
    public void initTika() {
        tika = new Tika();
    }

    public static void saveImageWithUserEmail(String email, List<MultipartFile> images) {
        int idx = 1;

        if (images == null) {
            throw new IllegalArgumentException("회원의 신분을 입증할 파일이 없습니다.");
        }

        try {
            for (MultipartFile image : images) {
                String extension = FilenameUtils.getExtension(image.getOriginalFilename());
                File destImage = generateFile(email, idx, extension);
                image.transferTo(destImage);
                log.info("Save file: {}", destImage.getName());
                idx++;
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("파일을 저장하는 데 실패하였습니다.");
        }
    }

    public static Set<Image> saveImageWithBoardId(final GroupBoard board,
                                                  final List<MultipartFile> images,
                                                  final String domainInfo) {
        int idx = 1;

        Set<Image> imageEntities = new HashSet<>();
        try {
            if (!CollectionUtils.isEmpty(images)) {
                for (MultipartFile image : images) {
                    String extension = FilenameUtils.getExtension(image.getOriginalFilename());
                    Long groupId = board.getId();
                    File destImage = generateFile(String.valueOf(groupId), idx, extension);
                    image.transferTo(destImage);
                    log.info("Save file: {}", destImage.getName());
                    idx++;

                    imageEntities.add(
                            Image.of(destImage, board, image.getOriginalFilename(), domainInfo)
                    );
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        return imageEntities;
    }

    public static String getMimeType(final MultipartFile file) {
        try {
            return tika.detect(file.getBytes());
        } catch (IOException e) {
            throw new FileNotReadableException();
        }
    }

    private static File generateFile(final String id, final int idx, final String extension) {
        File file = new File(
                IMAGE_SAVE_PATH + id + "/" + id + "_" + idx + "." + extension
        );

        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }

}
