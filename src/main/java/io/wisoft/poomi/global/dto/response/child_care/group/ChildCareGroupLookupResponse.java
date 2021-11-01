package io.wisoft.poomi.global.dto.response.child_care.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.global.utils.LocalDateTimeUtils;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.util.Date;

@Getter
@Setter
public class ChildCareGroupLookupResponse {

    private Long id;

    @JsonProperty("group_name")
    private String groupName;

    private String writer;

    @JsonProperty("regular_meeting_day")
    private String regularMeetingDay;

    @JsonProperty("main_activity")
    private String mainActivity;

    @JsonProperty("including_members")
    private Integer includingMembers;

    @JsonProperty("profile_image_uri")
    private String profileImageURI;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    public ChildCareGroupLookupResponse(final ChildCareGroup childCareGroup) {
        this.id = childCareGroup.getId();
        this.groupName = childCareGroup.getName();
        this.writer = childCareGroup.getWriter().getNick();
        this.regularMeetingDay = childCareGroup.getRegularMeetingDay();
        this.mainActivity = childCareGroup.getMainActivity();
        this.includingMembers = childCareGroup.getParticipatingMembers().size();
        this.profileImageURI = childCareGroup.getProfileImage().getFileAccessURI();
        this.createdAt = LocalDateTimeUtils.toDate(childCareGroup.getCreatedAt());
        this.requestedAt = new Date();
    }

    public static ChildCareGroupLookupResponse of(final ChildCareGroup childCareGroup) {
        return new ChildCareGroupLookupResponse(childCareGroup);
    }

    /**
     * 테스트 코드 확인 시 json parsing 을 위한 생성자
     * @param id
     * @param groupName
     * @param writer
     * @param requestedAt
     */
    @ConstructorProperties({"id", "title", "writer", "requested_at"})
    public ChildCareGroupLookupResponse(final Long id,
                                        final String groupName, final String writer,
                                        final Date requestedAt) {
        this.id = id;
        this.groupName = groupName;
        this.writer = writer;
        this.requestedAt = requestedAt;
    }

}
