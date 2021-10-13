package io.wisoft.poomi.controller;

import io.wisoft.poomi.domain.child_care.group.image.Image;
import io.wisoft.poomi.domain.child_care.group.image.ImageRepository;
import io.wisoft.poomi.global.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
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
import java.io.FileInputStream;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ImageController {

    private final ImageRepository imageRepository;

    @GetMapping("/image/{image_name}")
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> viewImage(
            @PathVariable("image_name") @Valid final String imageName) {
        Image image = imageRepository.getImageByImageName(imageName);

        String imagePath = image.getImagePath();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(FileUtils.findFileByPath(imagePath), headers, HttpStatus.OK);
    }

}
