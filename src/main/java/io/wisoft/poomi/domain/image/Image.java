package io.wisoft.poomi.domain.image;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.File;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
    name = "image_sequence_generator",
    sequenceName = "image_sequence",
    initialValue = 1,
    allocationSize = 1
)
public class Image {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "image_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "image_access_uri")
    private String imageAccessURI;

    @Column(name = "image_download_uri")
    private String imageDownloadURI;

    @Column(name = "content_type")
    private String contentType;

    @Builder
    public Image(final String imageName,
                 final String imageAccessURI, final String imageDownloadURI,
                 final String contentType) {
        this.imageName = imageName;
        this.imageAccessURI = imageAccessURI;
        this.imageDownloadURI = imageDownloadURI;
        this.contentType = contentType;
    }

    public static Image of(final String imageName,
                           final String imageAccessURI, final String imageDownloadURI,
                           final String contentType) {
        return Image.builder()
                .imageName(imageName)
                .imageAccessURI(imageAccessURI)
                .imageDownloadURI(imageDownloadURI)
                .contentType(contentType)
                .build();
    }

}
