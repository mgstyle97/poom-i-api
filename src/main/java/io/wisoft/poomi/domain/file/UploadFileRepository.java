package io.wisoft.poomi.domain.file;

import io.wisoft.poomi.global.exception.exceptions.NotFoundEntityDataException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {

    Optional<UploadFile> findByFileName(final String fileName);

    default UploadFile getFileByFileName(final String fileName) {
        return this.findByFileName(fileName).orElseThrow(
                () -> new NotFoundEntityDataException("file name: " + fileName + "에 관한 데이터를 찾지 못했습니다.")
        );
    }

}
