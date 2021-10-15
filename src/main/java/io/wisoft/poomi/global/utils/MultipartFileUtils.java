package io.wisoft.poomi.global.utils;

import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.board.GroupBoard;
import io.wisoft.poomi.domain.image.Image;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.exception.exceptions.FileNotReadableException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class MultipartFileUtils {

    private static final String IMAGE_SAVE_PATH = System.getProperty("user.dir") + "/img/";
    private static final Tika tika = new Tika();

    private enum DomainType{
        MEMBER, GROUP, BOARD
    }

    public static byte[] findFileByteArray(final String filePath) {
        try(final FileInputStream fileInputStream = new FileInputStream(filePath)) {

            return IOUtils.toByteArray(fileInputStream);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error to read image file");
        }
    }

    public static void saveProfileImage(final MultipartFile profileImage, final Member member) {
        try {
            final String extension = FilenameUtils.getExtension(profileImage.getOriginalFilename());
            File profileFile = generateFile(
                    getFilePath(DomainType.MEMBER, member),
                    member.getNick() + "profile." + extension
            );
            profileImage.transferTo(profileFile);
            log.info("Save member profile image: {}", profileFile.getName());

            member.saveProfileImagePath(profileFile.getPath());
        } catch (IOException e) {
            throw new IllegalArgumentException("프로필 이미지를 저장하는데 실패하였습니다.");
        }
    }

    public static void saveImageWithUserEmail(final Member member, final List<MultipartFile> images) {
        if (images == null) {
            throw new IllegalArgumentException("회원의 신분을 입증할 파일이 없습니다.");
        }

        try {
            for (MultipartFile image : images) {
                File destImage = generateFile(
                        getFilePath(DomainType.MEMBER, member) + "/doc/",
                        image.getOriginalFilename()
                );
                image.transferTo(destImage);
                log.info("Save file: {}", destImage.getName());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("파일을 저장하는데 실패하였습니다.");
        }
    }

    public static Image saveGroupProfileImage(final ChildCareGroup group,
                                              final MultipartFile profileImage,
                                              final String domainInfo) {
        try {
            File destProfileImage = generateFile(
                    getFilePath(DomainType.GROUP, group),
                    profileImage.getOriginalFilename()
            );
            profileImage.transferTo(destProfileImage);

            return Image.of(
                    destProfileImage,
                    profileImage.getOriginalFilename(),
                    domainInfo + "/api/profile-image/" + group.getName());
        } catch (IOException e) {
            throw new IllegalArgumentException("품앗이반 프로필 이미지를 저장하는데 실패하였습니다.");
        }
    }

    public static Set<Image> saveImageWithBoardId(final GroupBoard board,
                                                  final List<MultipartFile> images,
                                                  final String domainInfo) {
        Set<Image> imageEntities = new HashSet<>();
        try {
            if (!CollectionUtils.isEmpty(images)) {
                for (MultipartFile image : images) {
                    File destImage = generateFile(
                            getFilePath(DomainType.BOARD, board),
                            image.getOriginalFilename()
                    );
                    image.transferTo(destImage);
                    log.info("Save file: {}", destImage.getName());

                    imageEntities.add(
                            Image.of(destImage, image.getOriginalFilename(), domainInfo)
                    );
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("게시글의 이미지를 저장하는데 실패하였습니다.");
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

    public static String getMimeType(final byte[] fileData) {
        try {
            InputStream is = new BufferedInputStream(new ByteArrayInputStream(fileData));
            return URLConnection.guessContentTypeFromStream(is);
        } catch (IOException e) {
            throw new FileNotReadableException();
        }

    }

    private static File generateFile(final String filePath, final String fileName) {
        File file = new File(
                filePath + fileName
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

    private static String getFilePath(final DomainType domainType, Object domainObject) {
        switch (domainType) {
            case MEMBER: {
                Member member = (Member) domainObject;
                return IMAGE_SAVE_PATH + "member/" + member.getEmail() + "/";
            }
            case GROUP: {
                ChildCareGroup group = (ChildCareGroup) domainObject;
                return IMAGE_SAVE_PATH + "group/" + group.getName() + "/";
            }
            case BOARD: {
                GroupBoard board = (GroupBoard) domainObject;
                ChildCareGroup group = board.getChildCareGroup();
                return IMAGE_SAVE_PATH + "group/" + group.getName() + "/board/" + board.getId() + "/";
            }
            default: return null;
        }
    }

}


