package io.wisoft.poomi.global.dto.response.admin.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.Gender;
import io.wisoft.poomi.domain.member.child.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChildInfoResponse {

    private Long id;

    private String name;

    @JsonFormat(pattern = "yyyy.MM.dd")
    private Date birthday;

    private String school;

    @JsonProperty("special_note")
    private String specialNote;

    private Gender gender;

    @Builder
    public ChildInfoResponse(final Long id,
                             final String name, final Date birthday,
                             final String school, final String specialNote,
                             final Gender gender) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.school = school;
        this.specialNote = specialNote;
        this.gender = gender;
    }

    public static ChildInfoResponse of(final Child child) {
        return ChildInfoResponse.builder()
                .id(child.getId())
                .name(child.getName())
                .birthday(child.getBirthday())
                .school(child.getSchool())
                .specialNote(child.getSpecialNote())
                .gender(child.getGender())
                .build();
    }

}
