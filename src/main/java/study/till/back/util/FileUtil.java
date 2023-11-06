package study.till.back.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import study.till.back.dto.file.FileUploadDTO;
import study.till.back.exception.file.DeleteFileException;
import study.till.back.exception.file.UploadFileException;

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

    public static FileUploadDTO uploadFile(String uploadPath, MultipartFile file) {
        FileUploadDTO fileUploadDTO = new FileUploadDTO();

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
            fileUploadDTO.setResult(false);
            return fileUploadDTO;
        }

        fileUploadDTO.setOriginFileName(originFileName);
        fileUploadDTO.setSavedFileName(savedFileName);
        fileUploadDTO.setUploadDir(uploadPath);
        fileUploadDTO.setExtension(extension);
        fileUploadDTO.setSize(file.getSize());
        fileUploadDTO.setContentType(file.getContentType());

        try {
            fileUploadDTO.setResult(true);
            file.transferTo(uploadFile);
        } catch (IOException e) {
            log.error("Failed to upload file: ", e);
            throw new UploadFileException();
        }
        return fileUploadDTO;
    }

    public static boolean deleteFile(String savedFilePath) {
        File file = new File(savedFilePath);

        if (file.exists()) {
            if (!file.delete()) {
                throw new DeleteFileException();
            }
        }
        return true;
    }
}
