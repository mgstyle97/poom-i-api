package io.wisoft.poomi.controller;

import io.wisoft.poomi.global.utils.UploadFileUtils;
import io.wisoft.poomi.service.child_care.group.ChildCareGroupService;
import io.wisoft.poomi.service.file.S3FileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ImageController {

    private final S3FileHandler s3FileHandler;
    private final ChildCareGroupService childCareGroupService;

    @GetMapping("/image")
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> viewImage(
            @RequestParam("fileName") @Valid final String fileName) {
        final byte[] fileData = s3FileHandler.getFileData(fileName);

        HttpHeaders headers = getMimeTypeHeader(fileData);

        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadImage(@RequestParam("image") @Valid final String imageName) {
        final byte[] fileData = s3FileHandler.getFileData(imageName);

        String downloadName = getDownloadName(imageName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(fileData.length);
        headers.setContentDispositionFormData("attachment", downloadName);

        return new ResponseEntity<>(
                fileData, headers, HttpStatus.OK
        );
    }

    @GetMapping("/profile-image/{group-name}")
    public ResponseEntity<byte[]> getGroupProfileImage(@PathVariable("group-name") final String groupName) {

        final byte[] fileData = childCareGroupService.getProfileImage(groupName);

        HttpHeaders headers = getMimeTypeHeader(fileData);

        return new ResponseEntity<>(
                fileData, headers, HttpStatus.OK
        );
    }

    private HttpHeaders getMimeTypeHeader(final byte[] fileData) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, UploadFileUtils.getContentType(fileData));

        return headers;
    }

    private String getDownloadName(final String fileName) {
        try {
            return URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            log.error("다운로드 파일명을 인코딩하는데 실패하였습니다.");
            return "non-file-name";
        }
    }

}
