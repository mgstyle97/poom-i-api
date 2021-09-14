package io.wisoft.poomi.bind.dto.childminder.classes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.childminder.classes.ChildminderClass;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChildminderClassRegisterDto {

    private Long id;

    private String title;

    private String writer;

    @JsonProperty("registered_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registeredAt;

    @Builder
    private ChildminderClassRegisterDto(final Long id,
                                       final String title, final String writer) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.registeredAt = new Date();
    }

    public static ChildminderClassRegisterDto from(final ChildminderClass childminderClass) {
        ChildminderClassRegisterDto childminderClassRegisterDto = ChildminderClassRegisterDto.builder()
                .id(childminderClass.getId())
                .title(childminderClass.getTitle())
                .writer(childminderClass.getWriter().getName())
                .build();

        return childminderClassRegisterDto;
    }

}
