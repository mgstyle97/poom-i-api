package io.wisoft.poomi.global.utils;

import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.board.GroupBoard;
import io.wisoft.poomi.domain.image.Image;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.exception.exceptions.FileNotReadableException;
import io.wisoft.poomi.service.file.S3FileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLConnection;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class UploadFileUtils {

    private static final String IMAGE_SAVE_PATH = System.getProperty("user.dir") + "/img/";
    private final S3FileHandler s3FileHandler;
    private static final Tika tika = new Tika();

    private enum DomainType {
        MEMBER, GROUP, BOARD
    }

    private File saveFile(final String fileMetaData, final String extension) {
        final byte[] fileData = Base64Utils.decodeFromString(getFileData(fileMetaData));
        final UUID uuid = UUID.randomUUID();
        final String fileName = uuid.toString() + "." + extension;
        final File targetFile = new File(fileName);
        try {
            FileUtils.writeByteArrayToFile(targetFile, fileData);
            return targetFile;
        } catch (final IOException E) {
            log.error("파일을 저장하는데 실패하였습니다.");
            throw new IllegalArgumentException("전달받은 파일 형식이 잘못되어 저장하는데 실패하였습니다.");
        }

    }

    private void deleteFile(final String fileName) {
        File deleteFile = new File(fileName);
        if (deleteFile.exists()) {
            if (deleteFile.delete()) {
                log.info("로컬 파일을 성공적으로 삭제하였습니다.");
            } else {
                log.error("로컬 파일을 삭제하는데 실패하였습니다.");
            }
        } else {
            log.error("로컬에 존재하지 않는 파일 File name: {}", fileName);
        }
    }

    public Image saveFileAndConvertImage(final String fileData) {
        final String fileExtension = getFileExtension(fileData);
        String fileName = s3FileHandler.uploadFileData(saveFile(fileData, fileExtension));
        String fileAccessURI = s3FileHandler.getFileAccessURI(fileName);
        String fileDownloadURI = "/api/download?image=" + fileName;
        String contentType = "image/" + fileExtension;

        deleteFile(fileName);

        return Image.of(fileName, fileAccessURI, fileDownloadURI, contentType);
    }

    public void removeBoardImages(final Set<Image> images) {
        images
                .forEach(image -> s3FileHandler.deleteFile(image.getImageName()));

    }

    public void removeImage(final Image image) {
        s3FileHandler.deleteFile(image.getImageName());
    }

    public static String getMimeType(final File file) {
        try {
            return tika.detect(file);
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

    public String getFileExtension(final String fileData) {
        int extensionStartIdx = fileData.indexOf("/") + 1;
        int extensionEndIdx = fileData.indexOf(";");

        return fileData.substring(extensionStartIdx, extensionEndIdx);
    }

    public String getFileData(final String fileMetaData) {
        int dataStartIdx = fileMetaData.indexOf(",") + 1;

        return fileMetaData.substring(dataStartIdx);
    }

}


