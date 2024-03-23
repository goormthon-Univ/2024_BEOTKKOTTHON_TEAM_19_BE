package com.example.feelsun.config.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<String> uploadMultipleFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            if (!file.isEmpty()) {
                fileUrls.add(saveFile(file));
            }
        }

        return fileUrls;
    }

    public String uploadSingleFile(MultipartFile multipartFile) throws IOException {
        String fileUrl = "";

        if (!multipartFile.isEmpty()) {
            fileUrl = saveFile(multipartFile);
        }

        return fileUrl;
    }


    public String saveFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        // 확장자를 얻기 위해 파일 이름을 "." 기준으로 분리하기
        String[] fileNameParts = originalFilename.split("\\.");
        String extension = fileNameParts[fileNameParts.length - 1];

        // 파일 이름에 UUID를 추가하여 고유한 이름을 생성하기
        String uniqueFileName = UUID.randomUUID() + "." + extension;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, uniqueFileName, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, uniqueFileName).toString();
    }

    public void deleteImage(String originalFilename)  {
        amazonS3.deleteObject(bucket, originalFilename);
    }
}
