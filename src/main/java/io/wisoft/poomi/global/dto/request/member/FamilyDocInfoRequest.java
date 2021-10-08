package io.wisoft.poomi.global.dto.request.member;

import io.wisoft.poomi.configures.web.validator.image.Image;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class FamilyDocInfoRequest {

    @Image
    List<MultipartFile> images;

}
