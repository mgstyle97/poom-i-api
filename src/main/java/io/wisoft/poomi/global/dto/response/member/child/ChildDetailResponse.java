package io.wisoft.poomi.global.dto.response.member.child;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.group.participating.GroupParticipatingMember;
import io.wisoft.poomi.domain.member.Gender;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.global.dto.response.child_care.expert.ChildDoingExpertResponse;
import io.wisoft.poomi.global.dto.response.child_care.group.ChildCareGroupSimpleResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class ChildDetailResponse {

    @JsonProperty("child_id")
    private Long childId;

    @JsonProperty("child_name")
    private String childName;

    @JsonFormat(pattern = "yyyy.MM.dd")
    private Date birthday;

    private String school;

    private Gender gender;

    @JsonProperty("special_note")
    private String specialNote;

    @JsonProperty("expert_info")
    private ChildDoingExpertResponse expertInfo;

    @JsonProperty("group_info")
    private List<ChildCareGroupSimpleResponse> groupInfo;

    @Builder
    public ChildDetailResponse(final Long childId, final String childName,
                               final Date birthday, final String school,
                               final String specialNote, final Gender gender,
                               final ChildDoingExpertResponse expertInfo,
                               final List<ChildCareGroupSimpleResponse> groupInfo) {
        this.childId = childId;
        this.childName = childName;
        this.birthday = birthday;
        this.school = school;
        this.specialNote = specialNote;
        this.gender = gender;
        this.expertInfo = expertInfo;
        this.groupInfo = groupInfo;
    }

    public static ChildDetailResponse of(final Child child) {

        ChildDoingExpertResponse expertInfo = generateExportInfo(child);
        List<ChildCareGroupSimpleResponse> groupInfo = generateGroupInfo(child);

        return ChildDetailResponse.builder()
                .childId(child.getId())
                .childName(child.getName())
                .birthday(child.getBirthday())
                .school(child.getSchool())
                .specialNote(child.getSpecialNote())
                .gender(child.getGender())
                .expertInfo(expertInfo)
                .groupInfo(groupInfo)
                .build();
    }

    private static ChildDoingExpertResponse generateExportInfo(final Child child) {
        ChildDoingExpertResponse expertInfo = null;
        if (Optional.ofNullable(child.getCaredExpertContent()).isPresent()) {
            expertInfo = ChildDoingExpertResponse.of(child.getCaredExpertContent());
        }

        return expertInfo;
    }

    private static List<ChildCareGroupSimpleResponse> generateGroupInfo(final Child child) {
        return child.getParticipatingGroups().stream()
                .map(GroupParticipatingMember::getGroup)
                .map(ChildCareGroupSimpleResponse::of)
                .collect(Collectors.toList());
    }

}
