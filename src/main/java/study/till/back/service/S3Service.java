package study.till.back.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import study.till.back.dto.file.FileUploadDTO;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3Client amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public FileUploadDTO uploadFile(MultipartFile multipartFile) {
        String originFileName = multipartFile.getOriginalFilename();
        String extension = makeExtension(originFileName);
        String uploadPath = makeUploadPath();
        String savedFileName = makeSavedFileName(extension);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        FileUploadDTO fileUploadDTO = new FileUploadDTO();
        fileUploadDTO.setOriginFileName(originFileName);
        fileUploadDTO.setSavedFileName(savedFileName);
        fileUploadDTO.setUploadDir(uploadPath);
        fileUploadDTO.setExtension(extension);
        fileUploadDTO.setSize(multipartFile.getSize());
        fileUploadDTO.setContentType(multipartFile.getContentType());
        fileUploadDTO.setResult(true);

        try {
            amazonS3.putObject(bucket, uploadPath + savedFileName, multipartFile.getInputStream(), metadata);
        }
        catch (IOException e) {
            log.error("Failed to upload file: ", e);
        }

        return fileUploadDTO;

    }

    public boolean deleteFile(String savedFilePath) {
        try {
            if(!amazonS3.doesObjectExist(bucket, savedFilePath)) {
                return false;
            }
            amazonS3.deleteObject(bucket, savedFilePath);
            return true;
        }
        catch (Exception e) {
            log.error("Failed to delete file: ", e);
            return false;
        }
    }


    public String makeUploadPath() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String nowTime = localDateTime.format(DateTimeFormatter.ofPattern("yyMMdd"));
        return nowTime + "/";
    }
    public String makeSavedFileName(String extension) {
        return UUID.randomUUID() + extension;
    }

    public String makeExtension(String originFileName) {
        return originFileName.substring(originFileName.lastIndexOf("."));
    }
}
