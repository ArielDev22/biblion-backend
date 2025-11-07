package com.projeto_integrado_biblioteca.domains.book.storage;

import com.projeto_integrado_biblioteca.config.S3ConfigProps;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final S3Client s3;
    private final S3ConfigProps props;

    @PostConstruct
    public void ensureBucket() {
        String bucket = props.bucket();
        try {
            s3.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
        } catch (S3Exception e) {
            s3.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
        }
    }

    public String uploadPdf(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio");
        }

        String key = UUID.randomUUID() + ".pdf";

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(props.bucket())
                .key(key)
                .contentType(MediaType.APPLICATION_PDF_VALUE)
                .contentLength(file.getSize())
                .build();

        try {
            s3.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException("Falha no upload ao MinIO", e);
        }

        return key;
    }


    public String uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio");
        }

        String key = UUID.randomUUID() + ".jpg";

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(props.bucket())
                .key(key)
                .contentType(MediaType.IMAGE_JPEG_VALUE)
                .contentLength(file.getSize())
                .build();

        try {
            s3.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException("Falha no upload ao MinIO", e);
        }

        return key;
    }
}
