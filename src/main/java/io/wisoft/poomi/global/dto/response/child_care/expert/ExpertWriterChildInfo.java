package io.wisoft.poomi.global.dto.response.child_care.expert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.child.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ExpertWriterChildInfo {

    @JsonProperty("child_id")
    private Long childId;

    @JsonProperty("child_name")
    private String childName;

    @JsonProperty("child_birthday")
    @JsonFormat(pattern = "yyyy.MM.dd")
    private Date childBirthday;

    @Builder
    public ExpertWriterChildInfo(final Long childId,
                                 final String childName, final Date childBirthday) {
        this.childId = childId;
        this.childName = childName;
        this.childBirthday = childBirthday;
    }

    public static ExpertWriterChildInfo of(final Child child) {
        return ExpertWriterChildInfo.builder()
                .childId(child.getId())
                .childName(child.getName())
                .childBirthday(child.getBirthday())
                .build();
    }

}
