package io.wisoft.poomi.domain.image;

import io.wisoft.poomi.global.exception.exceptions.NotFoundEntityDataException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByImageName(final String imageName);

    default Image getImageByImageName(final String imageName) {
        return this.findByImageName(imageName).orElseThrow(
                () -> new NotFoundEntityDataException("image name: " + imageName + "에 관한 데이터를 찾지 못했습니다.")
        );
    }

}
