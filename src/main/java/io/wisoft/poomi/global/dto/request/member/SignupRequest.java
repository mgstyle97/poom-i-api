package io.wisoft.poomi.global.dto.request.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class SignupRequest {

    @NotBlank(message = "이름을 입력해야 합니다.")
    private String name;

    @NotBlank(message = "핸드폰 번호를 입력해야 합니다.")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @Email(message = "이메일 형식이 부적절합니다.")
    @NotBlank(message = "이메일을 입력해야 합니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해야 합니다.")
    private String password;

    @NotBlank(message = "닉네임을 입력해야 합니다.")
    private String nick;

    private Gender gender;

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

    @JsonProperty("children")
    private List<ChildAddRequest> children;

    public void setGender(final String gender) {
        this.gender = Gender.valueOf(gender);
    }

    public Gender getGender() {
        return this.gender;
    }

}
