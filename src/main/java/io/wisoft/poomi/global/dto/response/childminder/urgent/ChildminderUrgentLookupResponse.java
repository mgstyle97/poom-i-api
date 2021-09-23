package io.wisoft.poomi.global.dto.response.childminder.urgent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.childminder.urgent.ChildminderUrgent;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChildminderUrgentLookupResponse {

    @JsonProperty("childminder_urgent_id")
    private Long childminderUrgentId;

    private String writer;

    private String contents;

    @JsonProperty("registered_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registeredAt;

    @Builder
    private ChildminderUrgentLookupResponse(final Long childminderUrgentId,
                                            final String writer, final String contents) {
        this.childminderUrgentId = childminderUrgentId;
        this.writer = writer;
        this.contents = contents;
        this.registeredAt = new Date();
    }

    public static ChildminderUrgentLookupResponse of(final ChildminderUrgent childminderUrgent) {
        return ChildminderUrgentLookupResponse.builder()
                .childminderUrgentId(childminderUrgent.getId())
                .writer(childminderUrgent.getWriter().getNick())
                .contents(childminderUrgent.getContents())
                .build();
    }

}
