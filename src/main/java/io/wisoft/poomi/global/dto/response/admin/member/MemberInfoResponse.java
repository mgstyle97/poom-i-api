package io.wisoft.poomi.global.dto.response.admin.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.file.UploadFile;
import io.wisoft.poomi.domain.member.Gender;
import io.wisoft.poomi.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class MemberInfoResponse {

    private Long id;

    private String name;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String email;

    private String nick;

    private Gender gender;

    private Integer age;

    @JsonProperty("family_certification_file_url")
    private String familyCertificationFileURL;

    @JsonProperty("children_info")
    private List<ChildInfoResponse> childrenInfo;

    @Builder
    public MemberInfoResponse(final Long id,
                              final String name, final String phoneNumber,
                              final String email, final String nick,
                              final Gender gender, final Integer age,
                              final String familyCertificationFileURL,
                              final List<ChildInfoResponse> childrenInfo) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nick = nick;
        this.gender = gender;
        this.age = age;
        this.familyCertificationFileURL = familyCertificationFileURL;
        this.childrenInfo = childrenInfo;
    }

    public static MemberInfoResponse of(final Member member) {
        return MemberInfoResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .email(member.getEmail())
                .nick(member.getNick())
                .gender(member.getGender())
                .age(member.getAge())
                .familyCertificationFileURL(
                        generateFamilyCertificationFileURL(Optional.ofNullable(member.getFamilyCertificationFile()))
                )
                .childrenInfo(
                        member.getChildren().stream()
                                .map(ChildInfoResponse::of)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private static String generateFamilyCertificationFileURL(final Optional<UploadFile> optionalUploadFile) {
        return optionalUploadFile.map(UploadFile::getFileAccessURI).orElse(null);

    }
}
