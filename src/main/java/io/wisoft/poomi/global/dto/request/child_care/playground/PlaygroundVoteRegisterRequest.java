package io.wisoft.poomi.global.dto.request.child_care.playground;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class PlaygroundVoteRegisterRequest {

    @JsonProperty("post_code")
    @Size(min = 5, max = 6, message = "우편번호 양식에 맞지 않습니다.")
    @NotBlank(message = "우편번호를 입력해야 합니다.")
    private String postCode;

    @NotBlank(message = "주소를 입력해야 합니다.")
    private String address;

    @JsonProperty("detail_address")
    @NotBlank(message = "상세 주소를 입력해야 합니다.")
    private String detailAddress;

    @JsonProperty("extra_address")
    @NotBlank(message = "부가 주소를 입력해야 합니다.")
    private String extraAddress;

    @JsonProperty("purpose_using")
    private String purposeUsing;

    @JsonProperty("images")
    private String images;

}
