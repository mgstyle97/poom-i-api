package io.wisoft.poomi.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class MemberJoinDto {

    @NotBlank
    private String name;

    @NotBlank
    @JsonProperty("phone_number")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "유효하지 않은 번호 형식입니다.")
    private String phoneNumber;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String nick;

    @NotBlank
    private String address;

    @JsonProperty("is_babysitter")
    private boolean isBabysitter;

}
