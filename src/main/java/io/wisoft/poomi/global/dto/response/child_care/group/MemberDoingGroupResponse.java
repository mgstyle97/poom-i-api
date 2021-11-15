package io.wisoft.poomi.global.dto.response.child_care.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.RecruitmentStatus;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.participating.GroupParticipatingMember;
import io.wisoft.poomi.domain.child_care.playground.vote.ExpiredStatus;
import io.wisoft.poomi.domain.file.UploadFile;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.response.child_care.group.apply.GroupApplyDetailResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class MemberDoingGroupResponse {

    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("group_name")
    private String groupName;

    @JsonProperty("activity_time")
    private String activityTime;

    @JsonProperty("profile_image_url")
    private String profileImageURL;

    @JsonProperty("main_activity")
    private String mainActivity;

    @JsonProperty("description")
    private String description;

    @JsonProperty("recruitment_status")
    private RecruitmentStatus recruitmentStatus;

    @JsonProperty("participation_type")
    private ParticipationType participationType;

    @JsonProperty("participating_members")
    private List<MemberParticipatingGroupResponse> participatingMembers;

    @JsonProperty("apply_info")
    private List<GroupApplyDetailResponse> applyInfo;

    @Builder
    public MemberDoingGroupResponse(final Long groupId,
                                    final String groupName, final String activityTime,
                                    final String profileImageURL, final String mainActivity, final String description,
                                    final RecruitmentStatus recruitmentStatus,
                                    final ParticipationType participationType,
                                    final List<MemberParticipatingGroupResponse> participatingMembers,
                                    final List<GroupApplyDetailResponse> applyInfo) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.activityTime = activityTime;
        this.profileImageURL = profileImageURL;
        this.mainActivity = mainActivity;
        this.description = description;
        this.recruitmentStatus = recruitmentStatus;
        this.participationType = participationType;
        this.participatingMembers = participatingMembers;
        this.applyInfo = applyInfo;
    }

    public static MemberDoingGroupResponse of(final ChildCareGroup group, final Member member) {
        return MemberDoingGroupResponse.builder()
                .groupId(group.getId())
                .groupName(group.getName())
                .activityTime(group.getRegularMeetingDay())
                .profileImageURL(
                        Optional.ofNullable(group.getProfileImage()).map(UploadFile::getFileAccessURI).orElse(null)
                )
                .mainActivity(group.getMainActivity())
                .description(group.getDescription())
                .recruitmentStatus(group.getRecruitmentStatus())
                .participationType(generateParticipationType(group, member))
                .participatingMembers(generateParticipatingMembers(group, member))
                .applyInfo(generateApplyInfo(group))
                .build();
    }

    private static List<MemberParticipatingGroupResponse> generateParticipatingMembers(final ChildCareGroup group,
                                                                                       final Member member) {
        return group.getParticipatingMembers().stream()
                .filter(groupParticipatingMember ->
                        !(groupParticipatingMember.getMember().equals(member) &&
                                groupParticipatingMember.getParticipationType().equals(ParticipationType.MANAGE)))
                .map(MemberParticipatingGroupResponse::of)
                .collect(Collectors.toList());
    }

    private static List<GroupApplyDetailResponse> generateApplyInfo(final ChildCareGroup group) {
        return group.getApplies().stream()
                .map(GroupApplyDetailResponse::of)
                .collect(Collectors.toList());
    }

    private static ParticipationType generateParticipationType(final ChildCareGroup group, final Member member) {
        final boolean memberParticipatingAsManager = group.getParticipatingMembers().stream()
                .filter(participatingMember -> participatingMember.getMember().equals(member))
                .map(GroupParticipatingMember::getParticipationType)
                .anyMatch(participationType -> participationType.equals(ParticipationType.MANAGE));
        if (memberParticipatingAsManager) {
            return ParticipationType.MANAGE;
        }

        return ParticipationType.PARTICIPATION;
    }

}
