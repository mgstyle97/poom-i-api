package io.wisoft.poomi.bind.dto.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChildDeleteDto {

    private Long id;

    @JsonProperty("member_id")
    private Long memberId;

    @JsonProperty("deleted_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime deletedAt;

    public ChildDeleteDto(final Long id, final Long memberId) {
        this.id = id;
        this.memberId = memberId;
        this.deletedAt = LocalDateTime.now();
    }

}
