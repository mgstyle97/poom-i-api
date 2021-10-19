package io.wisoft.poomi.controller;

import io.wisoft.poomi.global.dto.request.ImageToBase64MetaData;
import org.apache.commons.io.FileUtils;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @PostMapping("/image")
    public String metaData(@RequestBody @Valid final ImageToBase64MetaData imageToBase64MetaData) {

        final byte[] fileData = Base64Utils.decodeFromString(imageToBase64MetaData.getMetaData());

        final String filePath = System.getProperty("user.dir") + "/img/destForm.pdf";
        File dest = new File(filePath);
        try {
            FileUtils.writeByteArrayToFile(dest, fileData);
        } catch (IOException e) {
            System.out.println("파일 저장 에러");
        }

        return imageToBase64MetaData.getMetaData();
    }

}
