package com.projeto_integrado_biblioteca.domains.storage;

import com.projeto_integrado_biblioteca.config.S3ConfigProps;
import com.projeto_integrado_biblioteca.domains.book.models.BookFile;
import com.projeto_integrado_biblioteca.domains.download.DownloadableFile;
import com.projeto_integrado_biblioteca.exceptions.ResourceNotFoundException;
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
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

    private final Duration presignedExpiration = Duration.ofMinutes(60);

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    private final S3ConfigProps props;

    @PostConstruct
    public void ensureBucket() {
        String bucket = props.bucket();
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
        } catch (S3Exception e) {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
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
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(pdf.getBytes()));
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
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(image.getBytes()));
        } catch (IOException e) {
            throw new StorageException("Erro ao realizar o upload");
        }

        return key;
    }

    public String uploadImageAsStream(InputStream image) {
        String key = UUID.randomUUID() + ".jpg";

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(props.bucket())
                            .key(key)
                            .contentType(MediaType.IMAGE_JPEG_VALUE)
                            .build(),
                    RequestBody.fromInputStream(image, image.available())
            );
        } catch (IOException e) {
            throw new StorageException("Erro ao realizar o upload");
        }

        return key;
    }

    public DownloadableFile getPdfForDownload(BookFile pdfFile) {
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .bucket(props.bucket())
                .key(pdfFile.getFileKey())
                .build();

        try {
            ResponseInputStream<GetObjectResponse> objectResponse = s3Client.getObject(objectRequest);
            GetObjectResponse response = objectResponse.response();

            return new DownloadableFile(
                    objectResponse,
                    pdfFile.getFilename(),
                    response.contentType(),
                    response.contentLength()
            );
        } catch (S3Exception e) {
            throw new StorageException("Erro ao buscar o PDF");
        }
    }

    public ResponseInputStream<GetObjectResponse> getPdf(String key) {
        if (key.isBlank()) {
            throw new ResourceNotFoundException("O PDF para este livro ainda não foi registrado.");
        }

        GetObjectRequest getObjectRequest = GetObjectRequest
                .builder()
                .bucket(props.bucket())
                .key(key)
                .build();

        try {
            return s3Client.getObject(getObjectRequest);
        } catch (S3Exception e) {
            LOGGER.error("Erro ao buscar o pdf: " + e.getMessage());
            throw new StorageException("Falha ao buscar o pdf");
        }
    }

    public String getCoverURL(String key) {
        try {
            GetObjectPresignRequest objectPresignRequest = GetObjectPresignRequest
                    .builder()
                    .signatureDuration(presignedExpiration)
                    .getObjectRequest(obj -> obj.bucket(props.bucket()).key(key))
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(objectPresignRequest);

            return presignedRequest.url().toString();
        } catch (S3Exception e) {
            throw new StorageException("Erro ao buscar a URL");
        }
    }

    public void deletePdf(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .key(key)
                .bucket(props.bucket())
                .build();
        try {
            s3Client.deleteObject(deleteObjectRequest);

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
            s3Client.deleteObject(deleteObjectRequest);

            LOGGER.info(key + " foi deletada");
        } catch (S3Exception e) {
            LOGGER.error("Erro ao deletar a imagem: " + e.getMessage());
            throw new StorageException("Erro ao alterar a imagem");
        }
    }
}
