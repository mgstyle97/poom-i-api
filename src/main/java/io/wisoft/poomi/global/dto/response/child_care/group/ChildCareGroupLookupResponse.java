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

    private String title;

    private String writer;

    @JsonProperty("regular_meeting_day")
    private String regularMeetingDay;

    @JsonProperty("main_activity")
    private String mainActivity;

    @JsonProperty("including_members")
    private Integer includingMembers;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    public ChildCareGroupLookupResponse(final ChildCareGroup childCareGroup) {
        this.id = childCareGroup.getId();
        this.title = childCareGroup.getTitle();
        this.writer = childCareGroup.getWriter().getNick();
        this.regularMeetingDay = childCareGroup.getRegularMeetingDay();
        this.mainActivity = childCareGroup.getMainActivity();
        this.includingMembers = childCareGroup.getParticipatingMembers().size();
        this.createdAt = LocalDateTimeUtils.toDate(childCareGroup.getCreatedAt());
        this.requestedAt = new Date();
    }

    public static ChildCareGroupLookupResponse of(final ChildCareGroup childCareGroup) {
        return new ChildCareGroupLookupResponse(childCareGroup);
    }

    /**
     * 테스트 코드 확인 시 json parsing 을 위한 생성자
     * @param id
     * @param title
     * @param writer
     * @param requestedAt
     */
    @ConstructorProperties({"id", "title", "writer", "requested_at"})
    public ChildCareGroupLookupResponse(final Long id,
                                        final String title, final String writer,
                                        final Date requestedAt) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.requestedAt = requestedAt;
    }

}
