package io.wisoft.poomi.global.utils;

import io.wisoft.poomi.domain.file.UploadFile;
import io.wisoft.poomi.global.dto.request.file.FileDataOfBase64;
import io.wisoft.poomi.global.exception.exceptions.FileNotReadableException;
import io.wisoft.poomi.service.file.S3FileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.io.*;
import java.net.URLConnection;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class UploadFileUtils {

    private final S3FileHandler s3FileHandler;

    private File saveFile(final String fileMetaData, final String extension) {
        final byte[] fileData = Base64Utils.decodeFromString(getFileMetaData(fileMetaData));
        final UUID uuid = UUID.randomUUID();
        final String fileName = uuid + extension;
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

    public UploadFile saveFileAndConvertImage(final String fileData) {
        final FileDataOfBase64 fileDataOfBase64 = convertToFileData(fileData);
        String fileName = s3FileHandler.uploadFileData(fileDataOfBase64);
        String fileAccessURI = s3FileHandler.getFileAccessURI(fileName);
        String fileDownloadURI = "/api/download?image=" + fileName;
        String contentType = fileDataOfBase64.getContentType();

        deleteFile(fileName);

        return UploadFile.of(fileName, fileAccessURI, fileDownloadURI, contentType);
    }

    public FileDataOfBase64 convertToFileData(final String fileData) {
        final String contentType = getContentType(fileData);
        final String fileMetaData = getFileMetaData(fileData);
        final File convertedOfMetaData = saveFile(fileMetaData, convertContentTypeToExtension(contentType));

        return FileDataOfBase64.builder()
                .contentType(contentType)
                .fileMetaData(fileMetaData)
                .convertedOfMetaData(convertedOfMetaData)
                .build();
    }

    public void removeBoardImages(final Set<UploadFile> uploadFiles) {
        uploadFiles
                .forEach(image -> s3FileHandler.deleteFile(image.getFileName()));

    }

    public void removeImage(final UploadFile uploadFile) {
        s3FileHandler.deleteFile(uploadFile.getFileName());
    }

    public static String getContentType(final byte[] fileData) {
        try {
            InputStream is = new BufferedInputStream(new ByteArrayInputStream(fileData));
            return URLConnection.guessContentTypeFromStream(is);
        } catch (IOException e) {
            throw new FileNotReadableException();
        }

    }

    public static String getContentType(final String fileData) {
        int contentTypeStartIdx = fileData.indexOf(":") + 1;
        int contentTypeEndIdx = fileData.indexOf(";");

        return fileData.substring(contentTypeStartIdx, contentTypeEndIdx);
    }

    public String getFileMetaData(final String fileMetaData) {
        int dataStartIdx = fileMetaData.indexOf(",") + 1;

        return fileMetaData.substring(dataStartIdx);
    }

    public String convertContentTypeToExtension(final String contentType) {
        try {
            return MimeTypes
                    .getDefaultMimeTypes()
                    .forName(contentType)
                    .getExtension();
        } catch (MimeTypeException e) {
            log.error("존재하지 않는 Content-Type: {}", contentType);
            throw new FileNotReadableException();
        }
    }

}


