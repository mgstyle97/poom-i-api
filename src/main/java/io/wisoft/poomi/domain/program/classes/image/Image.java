package io.wisoft.poomi.domain.program.classes.image;

import io.wisoft.poomi.domain.program.classes.ClassProgram;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;

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
    private String imageUri;

    @ManyToOne(
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "class_id",
        referencedColumnName = "id"
    )
    private ClassProgram classProgram;

    @Builder
    public Image(final String imageOriginalName, final String imageName,
                 final String imagePath, final String imageUri,
                 final ClassProgram classProgram) {
        this.imageOriginalName = imageOriginalName;
        this.imageName = imageName;
        this.imagePath = imagePath;
        this.imageUri = imageUri;
        this.classProgram = classProgram;
    }

    public static Image of(final File imageFile,
                           final ClassProgram classProgram,
                           final String imageOriginalName) {
        return Image.builder()
            .imageOriginalName(imageOriginalName)
            .imageName(imageFile.getName())
            .imagePath(imageFile.getPath())
            .imageUri(classProgram.getId() + "/" + imageFile.getName())
            .classProgram(classProgram)
            .build();
    }

}
