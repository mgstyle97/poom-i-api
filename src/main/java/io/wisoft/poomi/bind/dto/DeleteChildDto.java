package io.wisoft.poomi.bind.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DeleteChildDto {

    private Long id;

    @JsonProperty("member_id")
    private Long memberId;

    @JsonProperty("deleted_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime deletedAt;

    public DeleteChildDto(final Long id, final Long memberId) {
        this.id = id;
        this.memberId = memberId;
        this.deletedAt = LocalDateTime.now();
    }

}
