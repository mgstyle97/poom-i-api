package io.wisoft.poomi.bind.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.child.Child;
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

    @NotBlank(message = "로그인 아이디를 입력해야 합니다.")
    @JsonProperty("login_id")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력해야 합니다.")
    private String password;

    @NotBlank(message = "닉네임을 입력해야 합니다.")
    private String nick;

    @NotBlank(message = "주소 정보를 입력해야 합니다.")
    @JsonProperty("address")
    private AddressRegisterRequest address;

    private List<Child> children;

}
