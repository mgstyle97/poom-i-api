package io.wisoft.poomi.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberModifiedDto {

    private String email;
    private String password;
    @JsonProperty("modified_properties")
    private ModifiedProperties modifiedProperties;

}
