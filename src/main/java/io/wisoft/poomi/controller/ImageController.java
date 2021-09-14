package io.wisoft.poomi.controller;

import io.wisoft.poomi.domain.childminder.classes.image.Image;
import io.wisoft.poomi.domain.childminder.classes.image.ImageRepository;
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

        try(FileInputStream inputStream = new FileInputStream(imagePath)) {

            byte[] byteImage = IOUtils.toByteArray(inputStream);

            return new ResponseEntity<>(byteImage, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error to read image file");
        }
    }

}
