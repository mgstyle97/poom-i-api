package io.wisoft.poomi.bind.request.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
public class CMInfoRegisterRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @NotBlank(message = "품앗이 정보에 대한 자세한 설명을 입력해주세요.")
    private String experience;
    private String greeting;

}