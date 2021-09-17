package io.wisoft.poomi.global.dto.response.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPageDto {

    private String email;

    private String password;

    private String name;

    private String gender;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;

    @JsonProperty("child_count")
    private Integer childCount;

    @Builder
    public MyPageDto(final String email, final String password,
                     final String name, final String gender,
                     final String phoneNumber, final String address,
                     final Integer childCount) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.childCount = childCount;
    }

    public static MyPageDto of(final Member member) {
        MyPageDto myPageDto = MyPageDto.builder()
            .email(member.getEmail())
            .password(member.getPassword())
            .name(member.getName())
            .gender(member.getGender().toString())
            .phoneNumber(member.getPhoneNumber())
            .address(member.getAddress().getDetailAddress())
            .childCount(member.getChildren().size())
            .build();

        return myPageDto;
    }

}
