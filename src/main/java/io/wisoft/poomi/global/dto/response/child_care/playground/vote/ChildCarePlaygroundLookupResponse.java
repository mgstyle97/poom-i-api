package io.wisoft.poomi.global.dto.response.child_care.playground.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.playground.ChildCarePlayground;
import io.wisoft.poomi.domain.file.UploadFile;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ChildCarePlaygroundLookupResponse {

    @JsonProperty("playground_id")
    private Long playgroundId;

    private String name;

    @JsonProperty("operating_hours")
    private String operatingHours;

    private String holiday;

    @JsonProperty("call_number")
    private String callNumber;

    private String address;

    private String features;

    @JsonProperty("image_uris")
    private List<String> imageURIs;

    @Builder
    public ChildCarePlaygroundLookupResponse(final Long playgroundId, final String name,
                                             final String operatingHours, final String holiday,
                                             final String callNumber, final String address,
                                             final String features,
                                             final List<String> imageURIs) {
        this.playgroundId = playgroundId;
        this.name = name;
        this.operatingHours = operatingHours;
        this.holiday = holiday;
        this.callNumber = callNumber;
        this.address = address;
        this.features = features;
        this.imageURIs = imageURIs;
    }

    public static  ChildCarePlaygroundLookupResponse of(final ChildCarePlayground playground) {
        return ChildCarePlaygroundLookupResponse.builder()
                .playgroundId(playground.getId())
                .name(playground.getName())
                .operatingHours(playground.getOperatingHours())
                .holiday(playground.getHoliday())
                .callNumber(playground.getCallNumber())
                .address(playground.getAddress().getAddress())
                .features(playground.getFeatures())
                .imageURIs(
                        playground.getImages().stream()
                                .map(UploadFile::getFileAccessURI)
                                .collect(Collectors.toList())
                )
                .build();
    }

}
