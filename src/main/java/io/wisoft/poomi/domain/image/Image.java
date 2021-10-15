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

    @Column(name = "image_original_name")
    private String imageOriginalName;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "image_uri")
    private String imageURI;

    @Builder
    public Image(final String imageOriginalName, final String imageName,
                 final String imagePath, final String imageURI) {
        this.imageOriginalName = imageOriginalName;
        this.imageName = imageName;
        this.imagePath = imagePath;
        this.imageURI = imageURI;
    }

    public static Image of(final File imageFile,
                           final String imageOriginalName,
                           final String imageURI) {
        return Image.builder()
                .imageOriginalName(imageOriginalName)
                .imageName(imageFile.getName())
                .imagePath(imageFile.getPath())
                .imageURI(imageURI)
                .build();
    }

    public String getDirectoryPath() {
        int indexOfImageNameInPath = this.imagePath.indexOf(imageName);

        return this.imagePath.substring(0, indexOfImageNameInPath-1);
    }

}
