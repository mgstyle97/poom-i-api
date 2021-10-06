package io.wisoft.poomi.domain.member.profile_image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@SequenceGenerator(
        name = "profile_image_sequence_generator",
        sequenceName = "profile_image_sequence",
        initialValue = 1,
        allocationSize = 1
)
public class ProfileImage {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "profile_image_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    private String email;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Builder
    public ProfileImage(final String email,
                         final String profileImageUrl) {
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }


}
