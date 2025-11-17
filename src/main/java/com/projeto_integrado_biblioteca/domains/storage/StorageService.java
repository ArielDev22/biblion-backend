package com.projeto_integrado_biblioteca.domains.storage;

import com.projeto_integrado_biblioteca.config.S3ConfigProps;
import com.projeto_integrado_biblioteca.domains.book.BookPdfFile;
import com.projeto_integrado_biblioteca.domains.download.DownloadableFile;
import com.projeto_integrado_biblioteca.exceptions.StorageException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

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

    public String uploadPdf(MultipartFile pdf) {
        if (pdf.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio");
        }

        String key = UUID.randomUUID() + ".pdf";

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(props.bucket())
                .key(key)
                .contentType(MediaType.APPLICATION_PDF_VALUE)
                .contentLength(pdf.getSize())
                .build();

        try {
            s3.putObject(putObjectRequest, RequestBody.fromBytes(pdf.getBytes()));
        } catch (IOException e) {
            throw new StorageException("Erro ao realizar o upload");
        }

        return key;
    }

    public String uploadImage(MultipartFile image) {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio");
        }

        String key = UUID.randomUUID() + ".jpg";

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(props.bucket())
                .key(key)
                .contentType(MediaType.IMAGE_JPEG_VALUE)
                .contentLength(image.getSize())
                .build();

        try {
            s3.putObject(putObjectRequest, RequestBody.fromBytes(image.getBytes()));
        } catch (IOException e) {
            throw new StorageException("Erro ao realizar o upload");
        }

        return key;
    }

    public DownloadableFile getPdf(BookPdfFile pdfFile) {
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .bucket(props.bucket())
                .key(pdfFile.getPdfKey())
                .build();

        try {
            ResponseInputStream<GetObjectResponse> objectResponse = s3.getObject(objectRequest);
            GetObjectResponse response = objectResponse.response();

            return new DownloadableFile(
                    objectResponse,
                    pdfFile.getFilename(),
                    response.contentType(),
                    response.contentLength()
            );
        } catch (S3Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePdf(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .key(key)
                .bucket(props.bucket())
                .build();
        try {
            s3.deleteObject(deleteObjectRequest);

            LOGGER.info(key + " foi deletada");
        } catch (S3Exception e) {
            LOGGER.error("Erro ao deletar o pdf: " + e.getMessage());
            throw new StorageException("Erro ao alterar o pdf");
        }
    }

    public void deleteImage(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .key(key)
                .bucket(props.bucket())
                .build();

        try {
            s3.deleteObject(deleteObjectRequest);

            LOGGER.info(key + " foi deletada");
        } catch (S3Exception e) {
            LOGGER.error("Erro ao deletar a imagem: " + e.getMessage());
            throw new StorageException("Erro ao alterar a imagem");
        }
    }
}
