package io.wisoft.poomi.bind.dto.member;

import io.wisoft.poomi.bind.request.member.SignupRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class Dto {

    private SignupRequest signupRequest;

    private List<MultipartFile> multipartFiles;

}
