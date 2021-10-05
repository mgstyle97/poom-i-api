package io.wisoft.poomi.global.dto.response.child_care.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChildCareGroupRegisterResponse {

    private Long id;

    private String title;

    private String writer;

    @JsonProperty("registered_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registeredAt;

    @Builder
    private ChildCareGroupRegisterResponse(final Long id,
                                           final String title, final String writer) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.registeredAt = new Date();
    }

    public static ChildCareGroupRegisterResponse from(final ChildCareGroup childCareGroup) {
        ChildCareGroupRegisterResponse childCareGroupRegisterResponse = ChildCareGroupRegisterResponse.builder()
                .id(childCareGroup.getId())
                .title(childCareGroup.getTitle())
                .writer(childCareGroup.getWriter().getName())
                .build();

        return childCareGroupRegisterResponse;
    }

}
