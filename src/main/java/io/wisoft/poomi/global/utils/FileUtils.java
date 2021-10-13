package io.wisoft.poomi.global.utils;

import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.board.GroupBoard;
import io.wisoft.poomi.domain.child_care.group.image.Image;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.exception.exceptions.FileNotReadableException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class FileUtils {

    private static final String IMAGE_SAVE_PATH = System.getProperty("user.dir") + "/img/";
    private static final Tika tika = new Tika();

    public static byte[] findFileByPath(final String filePath) {
        try(final FileInputStream fileInputStream = new FileInputStream(filePath)) {

            return IOUtils.toByteArray(fileInputStream);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error to read image file");
        }
    }

    public static void saveProfileImage(final MultipartFile profileImage, final Member member) {
        try {
            final String extension = FilenameUtils.getExtension(profileImage.getOriginalFilename());
            File profileFile = new File(
                    IMAGE_SAVE_PATH + member.getEmail() + "/" + member.getNick() + "profile." + extension
            );
            profileImage.transferTo(profileFile);
            log.info("Save profile image: {}", profileFile.getName());

            member.saveProfileImagePath(profileFile.getPath());
        } catch (IOException e) {
            throw new IllegalArgumentException("프로필 이미지를 저장하는데 실패하였습니다.");
        }
    }

    public static void saveImageWithUserEmail(final String email, final List<MultipartFile> images) {
        if (images == null) {
            throw new IllegalArgumentException("회원의 신분을 입증할 파일이 없습니다.");
        }

        try {
            for (MultipartFile image : images) {
                String extension = FilenameUtils.getExtension(image.getOriginalFilename());
                File destImage = generateFile(email, image.getOriginalFilename(), extension);
                image.transferTo(destImage);
                log.info("Save file: {}", destImage.getName());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("파일을 저장하는데 실패하였습니다.");
        }
    }

    public static Set<Image> saveImageWithBoardId(final GroupBoard board,
                                                  final List<MultipartFile> images,
                                                  final String domainInfo) {
        Set<Image> imageEntities = new HashSet<>();
        try {
            if (!CollectionUtils.isEmpty(images)) {
                for (MultipartFile image : images) {
                    String extension = FilenameUtils.getExtension(image.getOriginalFilename());
                    Long groupId = board.getId();
                    File destImage = generateFile(String.valueOf(groupId), image.getOriginalFilename(), extension);
                    image.transferTo(destImage);
                    log.info("Save file: {}", destImage.getName());

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

    public static void removeBoardImages(final Set<Image> images) {
        if (images.size() != 0) {

            Image anyImage = images.stream().findAny().get();
            String directoryPath = anyImage.getDirectoryPath();

            for (Image image : images) {
                removeImage(image);
            }

            File directory = new File(directoryPath);
            removeFile(directory);
        }

    }

    public static void removeImage(final Image image) {
        try {
            File imageFile = new File(image.getImagePath());
            if (!removeFile(imageFile)) throw new IOException();
        } catch (IOException e) {
            log.error("파일을 삭제하는데 실패하였습니다.");
        }
    }

    public static String getMimeType(final MultipartFile file) {
        try {
            return tika.detect(file.getBytes());
        } catch (IOException e) {
            throw new FileNotReadableException();
        }
    }

    private static File generateFile(final String id, final String originalName, final String extension) {
        File file = new File(
                IMAGE_SAVE_PATH + id + "/" + id + "_" + originalName
        );

        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }

    private static boolean removeFile(final File file) {
        if (file.exists()) {
            return file.delete();
        }

        return false;
    }

}
