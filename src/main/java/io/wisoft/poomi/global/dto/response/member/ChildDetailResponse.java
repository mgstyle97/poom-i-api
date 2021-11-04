package io.wisoft.poomi.global.dto.response.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupSimpleDataResponse;
import io.wisoft.poomi.global.dto.response.child_care.expert.ChildCareExpertSimpleResponse;
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

    @JsonFormat(pattern = "yyyy.MM.dd")
    private Date birthday;

    private String school;

    @JsonProperty("special_note")
    private String specialNote;

    @JsonProperty("expert_info")
    private ChildCareExpertSimpleResponse expertInfo;

    @JsonProperty("group_info")
    private List<ChildCareGroupSimpleResponse> groupInfo;

    @Builder
    public ChildDetailResponse(final Long childId,
                               final Date birthday, final String school,
                               final String specialNote,
                               final ChildCareExpertSimpleResponse expertInfo,
                               final List<ChildCareGroupSimpleResponse> groupInfo) {
        this.childId = childId;
        this.birthday = birthday;
        this.school = school;
        this.specialNote = specialNote;
        this.expertInfo = expertInfo;
        this.groupInfo = groupInfo;
    }

    public static ChildDetailResponse of(final Child child) {

        ChildCareExpertSimpleResponse expertInfo = generateExportInfo(child);
        List<ChildCareGroupSimpleResponse> groupInfo = generateGroupInfo(child);

        return ChildDetailResponse.builder()
                .childId(child.getId())
                .birthday(child.getBirthday())
                .school(child.getSchool())
                .specialNote(child.getSpecialNote())
                .expertInfo(expertInfo)
                .groupInfo(groupInfo)
                .build();
    }

    private static ChildCareExpertSimpleResponse generateExportInfo(final Child child) {
        ChildCareExpertSimpleResponse expertInfo = null;
        if (Optional.ofNullable(child.getCaredExpertContent()).isPresent()) {
            expertInfo = ChildCareExpertSimpleResponse.of(child.getCaredExpertContent());
        }

        return expertInfo;
    }

    private static List<ChildCareGroupSimpleResponse> generateGroupInfo(final Child child) {
        return child.getParticipatingGroups().stream()
                .map(ChildCareGroupSimpleResponse::of)
                .collect(Collectors.toList());
    }

}
