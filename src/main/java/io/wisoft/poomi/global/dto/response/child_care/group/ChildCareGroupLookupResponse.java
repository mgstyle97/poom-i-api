package io.wisoft.poomi.global.dto.response.child_care.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
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

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    public ChildCareGroupLookupResponse(final ChildCareGroup childCareGroup) {
        this.id = childCareGroup.getId();
        this.title = childCareGroup.getTitle();
        this.writer = childCareGroup.getWriter().getName();
        this.requestedAt = new Date();
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
