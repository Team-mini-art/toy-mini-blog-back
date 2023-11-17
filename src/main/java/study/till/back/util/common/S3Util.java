package study.till.back.util.common;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class S3Util {

    private static AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private static String bucket;

    public static void uploadFile(MultipartFile multipartFile) throws IOException {
        String originalName = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalName, multipartFile.getInputStream(), metadata);
    }
}
