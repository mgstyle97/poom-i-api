package io.wisoft.poomi.global.dto.response.child_care.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.image.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ImageLookupResponse {

    @JsonProperty("image_id")
    private Long imageId;

    @JsonProperty("image_uri")
    private String imageURI;

    public static ImageLookupResponse of(final Image image) {
        return new ImageLookupResponse(image.getId(), image.getImageURI());
    }

}
