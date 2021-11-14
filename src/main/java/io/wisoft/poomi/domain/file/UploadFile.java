package io.wisoft.poomi.domain.file;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
    name = "upload_file_sequence_generator",
    sequenceName = "upload_file_sequence",
    initialValue = 13,
    allocationSize = 1
)
public class UploadFile {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "upload_file_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_access_uri")
    private String fileAccessURI;

    @Column(name = "file_download_uri")
    private String fileDownloadURI;

    @Column(name = "content_type")
    private String contentType;

    @Builder
    public UploadFile(final String fileName,
                      final String fileAccessURI, final String fileDownloadURI,
                      final String contentType) {
        this.fileName = fileName;
        this.fileAccessURI = fileAccessURI;
        this.fileDownloadURI = fileDownloadURI;
        this.contentType = contentType;
    }

    public static UploadFile of(final String fileName,
                                final String fileAccessURI, final String fileDownloadURI,
                                final String contentType) {
        return UploadFile.builder()
                .fileName(fileName)
                .fileAccessURI(fileAccessURI)
                .fileDownloadURI(fileDownloadURI)
                .contentType(contentType)
                .build();
    }

}
