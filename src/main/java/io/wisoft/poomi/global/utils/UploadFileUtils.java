package io.wisoft.poomi.global.utils;

import io.wisoft.poomi.domain.file.UploadFile;
import io.wisoft.poomi.global.dto.request.file.FileDataOfBase64;
import io.wisoft.poomi.global.exception.exceptions.FileNotReadableException;
import io.wisoft.poomi.service.file.S3FileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class UploadFileUtils {

    private final S3FileHandler s3FileHandler;

    private final SecretKey key;
    private final Cipher cipher;

    @Autowired
    public UploadFileUtils(final S3FileHandler s3FileHandler) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.s3FileHandler = s3FileHandler;
        this.key = KeyGenerator.getInstance("AES").generateKey();
        this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    }

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

    public UploadFile saveFileWithEncryption(final String fileData, final HttpServletRequest request) {
        final FileDataOfBase64 fileDataOfBase64 = convertToFileData(fileData);
        encryptFile(fileDataOfBase64);

        String fileName = s3FileHandler.uploadEncryptFileData(fileDataOfBase64);
        final String domain = getDomain(request);

        deleteFile(fileName);
        return UploadFile.builder()
                .fileName(fileName)
                .fileAccessURI(domain + "/api/image/encrypt?image=" + fileName)
                .fileDownloadURI(domain + "/api/download/encrypt?image=" + fileName)
                .contentType(fileDataOfBase64.getContentType())
                .build();
    }

    private void encryptFile(final FileDataOfBase64 fileDataOfBase64) {
        byte[] iv = new byte[0];
        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, this.key);
            iv = cipher.getIV();
        } catch (InvalidKeyException e) {
            log.error("IV 초기화 에러");
        }

        File encryptFile = new File(
                FilenameUtils.removeExtension(fileDataOfBase64.getConvertedOfMetaData().getName()) + ".enc"
        );

        try(
                FileOutputStream os = new FileOutputStream(encryptFile);
                CipherOutputStream cos = new CipherOutputStream(os, this.cipher)) {
            os.write(iv);
            cos.write(Files.readAllBytes(fileDataOfBase64.getConvertedOfMetaData().toPath()));
            fileDataOfBase64.setConvertedOfMetaData(encryptFile);
        } catch (IOException e) {
            log.error("파일 암호화 에러");
        }
    }

    public byte[] getEncryptFileToDecrypted(final String fileName) {
        byte[] encryptFileData = s3FileHandler.getFileData("encrypt/" + fileName);
        File encryptFile = new File(fileName);
        try {
            FileUtils.writeByteArrayToFile(encryptFile, encryptFileData);
        } catch (IOException e) {
            log.error("암호화된 파일을 로컬에 저장하는 데 실패하였습니다.");
        }

        try(FileInputStream is = new FileInputStream(fileName)) {
            byte[] iv = new byte[16];
            is.read(iv);
            this.cipher.init(Cipher.DECRYPT_MODE, this.key, new IvParameterSpec(iv));

            try(CipherInputStream cipherInputStream = new CipherInputStream(is, this.cipher)) {
                return IOUtils.toByteArray(cipherInputStream);
            } catch (IOException e) {
                log.error("파일 복호화 실패");
            }
        } catch (IOException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            log.error("파일 복호화를 위한 준비 실패");
        } finally {
            deleteFile(encryptFile.getName());
        }

        return null;
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
        uploadFiles.forEach(this::removeImage);
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

    private String getDomain(final HttpServletRequest request) {
        return request.getRequestURL()
                .toString()
                .replace(request.getRequestURI(), "");
    }

}


