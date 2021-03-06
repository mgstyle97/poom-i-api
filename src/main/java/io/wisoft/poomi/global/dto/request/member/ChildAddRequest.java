package io.wisoft.poomi.global.dto.request.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.Gender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class ChildAddRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @NotBlank(message = "자식의 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "자식이 다니는 학교명을 입력해주세요.")
    private String school;

    @JsonProperty("special_note")
    private String specialNote;

    @NotNull(message = "아이의 성별을 선택해주세요.")
    private Gender gender;

    public void setGender(final String gender) {
        this.gender = Gender.valueOf(gender);
    }

    public Gender getGender() {
        return this.gender;
    }

}
