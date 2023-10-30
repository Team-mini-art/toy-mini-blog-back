package study.till.back.dto.file;

import lombok.*;

@Data
@Setter
@Getter
public class UploadResult {
    private boolean result;
    private String originFileName;
    private String savedFileName;
    private String uploadDir;
    private String extension;
    private Long size;
    private String contentType;
}
