package io.wisoft.poomi.global.dto.request.child_care.playground;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ChildCarePlaygroundRegisterRequest {

    @JsonProperty("vote_id")
    @Min(1)
    @NotNull(message = "종료된 투표의 정보가 없습니다.")
    private Long voteId;

    @NotBlank(message = "품앗이터의 이름을 입력해주세요.")
    private String name;

    @JsonProperty("operating_hours")
    @NotBlank(message = "품앗이터의 운영 시간을 입력해주세요.")
    private String operatingHours;

    @NotBlank(message = "품앗이터의 휴일을 입력해주세요.")
    private String holiday;

    @JsonProperty("call_number")
    @NotBlank(message = "품앗이터의 전화번호를 입력해주세요.")
    private String callNumber;

    @NotBlank(message = "품앗이터의 소개를 입력해주세요.")
    private String features;

    @JsonProperty("images")
    private List<String> imageDataList;

}
