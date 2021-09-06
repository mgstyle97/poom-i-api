package io.wisoft.poomi.domain.program.classes.image;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByImageName(final String imageName);

    default Image getImageByImageName(final String imageName) {
        return this.findByImageName(imageName).orElseThrow(
                () -> new IllegalArgumentException("No image data by image name")
        );
    }

}
