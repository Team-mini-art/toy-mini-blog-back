package study.till.back.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import study.till.back.dto.file.UploadResult;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
public class FileUtil {

    public static boolean makeDirs(File uploadFile) {
        if (uploadFile.exists() && uploadFile.isDirectory()) {
            return true;
        }

        try {
            uploadFile.mkdirs();
            return true;
        }
        catch (Exception e) {
            log.error("Failed to create directories: ", e);
            return false;
        }
    }

    public static UploadResult uploadFile(String uploadPath, MultipartFile file) {
        UploadResult uploadResult = new UploadResult();

        LocalDateTime localDateTime = LocalDateTime.now();
        String nowTime = localDateTime.format(DateTimeFormatter.ofPattern("yyMMdd"));
        String originFileName = file.getOriginalFilename();
        String extension = originFileName.substring(originFileName.lastIndexOf("."));
        String savedFileName = UUID.randomUUID() + extension;

        uploadPath += nowTime + "/";
        File uploadFile = new File(uploadPath + savedFileName);

        FileUtil.makeDirs(uploadFile);

        if (!FileUtil.makeDirs(uploadFile)) {
            log.error("Failed to create directories for file: " + savedFileName);
            uploadResult.setResult(false);
            return uploadResult;
        }

        uploadResult.setOriginFileName(originFileName);
        uploadResult.setSavedFileName(savedFileName);
        uploadResult.setUploadDir(uploadPath);
        uploadResult.setExtension(extension);
        uploadResult.setSize(file.getSize());
        uploadResult.setContentType(file.getContentType());

        try {
            uploadResult.setResult(true);
            file.transferTo(uploadFile);
        } catch (IOException e) {
            log.error("Failed to upload file: ", e);
            uploadResult.setResult(false);
        }
        return uploadResult;
    }

    public static void deleteFile(String uploadPath) {

    }
}
