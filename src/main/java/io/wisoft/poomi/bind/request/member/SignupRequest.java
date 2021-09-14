package io.wisoft.poomi.bind.request.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class SignupRequest {

    @NotBlank(message = "이름을 입력해야 합니다.")
    private String name;

    @NotBlank(message = "핸드폰 번호를 입력해야 합니다.")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotBlank(message = "이메일을 입력해야 합니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해야 합니다.")
    private String password;

    @NotBlank(message = "닉네임을 입력해야 합니다.")
    private String nick;

    @NotBlank(message = "성별을 선택해야 합니다.")
    private String gender;

    @JsonProperty("address_info")
    private AddressRegisterRequest address;

    @JsonProperty("children")
    private List<ChildAddRequest> children;

}
