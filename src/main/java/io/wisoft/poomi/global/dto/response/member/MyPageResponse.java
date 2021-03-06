package io.wisoft.poomi.global.dto.response.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MyPageResponse {

    private String email;

    private String password;

    private String name;

    private String gender;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;

    @JsonProperty("detail_address")
    private String detailAddress;

    @JsonProperty("address_expired_date")
    @JsonFormat(pattern = "yyyy.MM.dd")
    private Date addressExpiredDate;

    @JsonProperty("child_count")
    private Integer childCount;

    @JsonProperty("member_score")
    private Integer memberScore;

    @Builder
    public MyPageResponse(final String email, final String password,
                          final String name, final String gender,
                          final String phoneNumber, final String address,
                          final String detailAddress,
                          final Date addressExpiredDate, final Integer childCount,
                          final Integer memberScore) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.detailAddress = detailAddress;
        this.addressExpiredDate = addressExpiredDate;
        this.childCount = childCount;
        this.memberScore = memberScore;
    }

    public static MyPageResponse of(final Member member, final Date addressExpiredDate) {
        MyPageResponse myPageResponse = MyPageResponse.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .name(member.getName())
                .gender(member.getGender().toString())
                .phoneNumber(member.getPhoneNumber())
                .address(member.getAddress().getDetailAddress())
                .detailAddress(member.getAddress().getDetailAddress())
                .addressExpiredDate(addressExpiredDate)
                .childCount(member.getChildren().size())
                .memberScore(member.getAverageScore())
                .build();

        return myPageResponse;
    }

}
