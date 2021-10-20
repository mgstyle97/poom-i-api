package io.wisoft.poomi.global.dto.request.file;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class FileDataOfBase64 {

    private String contentType;
    private String fileMetaData;
    private File convertedOfMetaData;

    @Builder
    public FileDataOfBase64(final String contentType, final String fileMetaData,
                            final File convertedOfMetaData) {
        this.contentType = contentType;
        this.fileMetaData = fileMetaData;
        this.convertedOfMetaData = convertedOfMetaData;
    }

}
