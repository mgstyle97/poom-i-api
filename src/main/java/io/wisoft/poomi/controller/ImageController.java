package io.wisoft.poomi.controller;

import io.wisoft.poomi.domain.image.Image;
import io.wisoft.poomi.domain.image.ImageRepository;
import io.wisoft.poomi.global.utils.MultipartFileUtils;
import io.wisoft.poomi.service.child_care.group.ChildCareGroupService;
import io.wisoft.poomi.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ImageController {

    private final ImageRepository imageRepository;
    private final MemberService memberService;
    private final ChildCareGroupService childCareGroupService;

    @GetMapping("/image/{image_name}")
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> viewImage(
            @PathVariable("image_name") @Valid final String imageName) {
        Image image = imageRepository.getImageByImageName(imageName);

        String imagePath = image.getImagePath();

        final byte[] fileData = MultipartFileUtils.findFileByteArray(imagePath);

        HttpHeaders headers = getMimeTypeHeader(fileData);

        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }

    @GetMapping("/profile-image/{nick}")
    public ResponseEntity<byte[]> getMemberProfileImage(@PathVariable("nick") @Valid final String nick) {

        final byte[] fileData = memberService.getMemberProfileImage(nick);

        HttpHeaders headers = getMimeTypeHeader(fileData);

        return new ResponseEntity<>(
                fileData, headers, HttpStatus.OK);
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
        headers.set(HttpHeaders.CONTENT_TYPE, MultipartFileUtils.getMimeType(fileData));

        return headers;
    }

}
