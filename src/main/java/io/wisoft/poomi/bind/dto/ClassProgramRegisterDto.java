package io.wisoft.poomi.bind.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.program.classes.ClassProgram;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ClassProgramRegisterDto {

    private Long id;

    private String title;

    private String writer;

    @JsonProperty("registered_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registeredAt;

    @Builder
    public ClassProgramRegisterDto(final Long id,
                                   final String title, final String writer) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.registeredAt = new Date();
    }

    public static ClassProgramRegisterDto from(final ClassProgram classProgram) {
        ClassProgramRegisterDto classProgramRegisterDto = ClassProgramRegisterDto.builder()
                .id(classProgram.getId())
                .title(classProgram.getTitle())
                .writer(classProgram.getWriter().getName())
                .build();

        return classProgramRegisterDto;
    }

}
