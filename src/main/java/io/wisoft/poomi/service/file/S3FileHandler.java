package io.wisoft.poomi.service.file;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import io.wisoft.poomi.global.aws.S3Bucket;
import io.wisoft.poomi.global.dto.request.file.FileDataOfBase64;
import io.wisoft.poomi.global.exception.exceptions.FileNotFoundException;
import io.wisoft.poomi.global.exception.exceptions.FileNotReadableException;
import io.wisoft.poomi.global.utils.UploadFileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3FileHandler {

    private final AmazonS3 amazonS3;
    private final S3Bucket s3Bucket;


    public byte[] getFileData(final String fileName) {
        try {
            S3Object s3Object = amazonS3.getObject(s3Bucket.getBucket(), fileName);
            S3ObjectInputStream inputStream = s3Object.getObjectContent();

            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            log.error("파일 읽기 실패 File name: {}", fileName);
            throw new FileNotReadableException();
        } catch (AmazonS3Exception e) {
            log.error("존재하지 않는 파일에 대한 요청 File name: {}", fileName);
            throw new FileNotFoundException();
        }

    }

    public String uploadFileData(final FileDataOfBase64 fileDataOfBase64) {
        final InputStream inputStream = getInputStream(fileDataOfBase64.getConvertedOfMetaData());
        final ObjectMetadata objectMetadata = getObjectMetaData(
                fileDataOfBase64.getConvertedOfMetaData(), fileDataOfBase64.getContentType()
        );

        uploadFile(fileDataOfBase64.getConvertedOfMetaData().getName(), inputStream, objectMetadata);
        return fileDataOfBase64.getConvertedOfMetaData().getName();
    }

    public String getFileAccessURI(final String fileName) {
        return amazonS3.getUrl(s3Bucket.getBucket(), fileName).toString();
    }

    public void deleteFile(final String fileName) {
        try {
            amazonS3.deleteObject(s3Bucket.getBucket(), fileName);
        } catch (AmazonS3Exception e) {
            log.error("존재하지 않는 파일에 대한 요청 File name: {}", fileName);
            throw new FileNotFoundException();
        }
    }

    private InputStream getInputStream(final File targetFile) {
        try {
            return new FileInputStream(targetFile);
        } catch (IOException e) {
            log.error("로컬 저장소에서 파일을 찾을 수 없습니다.");
            throw new FileNotFoundException();
        }
    }

    private ObjectMetadata getObjectMetaData(final File targetFile, final String contentType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(targetFile.length());
        objectMetadata.setContentType(contentType);

        return objectMetadata;
    }

    private void uploadFile(final String fileName, final InputStream inputStream, final ObjectMetadata objectMetadata) {
        try {
            amazonS3.putObject(
                    new PutObjectRequest(s3Bucket.getBucket(), fileName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );
        } catch (AmazonServiceException e) {
            log.error("파일을 스토리지 시스템에 업로드하는 것을 실패하였습니다.");
        }

    }

}
