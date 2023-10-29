package study.till.back.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
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
            log.info(e.getMessage());
            return false;
        }
    }

    public static Map<String, Object> uploadFile(String uploadPath, MultipartFile file) {
            LocalDateTime localDateTime = LocalDateTime.now();
            String nowTime = localDateTime.format(DateTimeFormatter.ofPattern("yyMMdd"));
            String originFileName = file.getOriginalFilename();
            String extension = originFileName.substring(originFileName.lastIndexOf("."));
            String savedFileName = nowTime + "/" + UUID.randomUUID() + extension;

            File uploadFile = new File(uploadPath + savedFileName);

            FileUtil.makeDirs(uploadFile);

            Map<String, Object> rtnMap = new HashMap<>();
            rtnMap.put("originFileName", originFileName);
            rtnMap.put("savedFileName", savedFileName);
            rtnMap.put("uploadPath", uploadPath);
            rtnMap.put("extension", extension);
            rtnMap.put("size", file.getSize());
            rtnMap.put("contentType", file.getContentType());

            try {
                rtnMap.put("result", true);
                file.transferTo(uploadFile);
            } catch (IOException e) {
                rtnMap.put("result", false);
                e.printStackTrace();
            }
            return rtnMap;
    }
}
