package com.projeto_integrado_biblioteca.domains.download;

import com.projeto_integrado_biblioteca.config.S3ConfigProps;
import com.projeto_integrado_biblioteca.domains.book.Book;
import com.projeto_integrado_biblioteca.domains.book.BookService;
import com.projeto_integrado_biblioteca.domains.user.User;
import com.projeto_integrado_biblioteca.domains.user.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.time.Instant;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class DownloadService {
    private final UserService userService;
    private final BookService bookService;
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


    // user autenticado
    public DownloadableFile download(Long bookId) {
        //User user = userService.getUserByEmail(username);
        Book book = bookService.getBookById(bookId);

        try {
            GetObjectRequest objectRequest = GetObjectRequest
                    .builder()
                    .bucket(props.bucket())
                    .key(book.getPdfKey())
                    .build();
            ResponseInputStream<GetObjectResponse> objectResponse = s3.getObject(objectRequest);
            GetObjectResponse response = objectResponse.response();

            return new DownloadableFile(
                    objectResponse,
                    generateFileName(book.getTitle()),
                    response.contentType(),
                    response.contentLength()
            );
        } catch (AwsServiceException e) {
            throw new RuntimeException(e);
        } catch (SdkClientException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateFileName(String title) {
        return title.replace(" ", "_").toLowerCase().concat(".pdf");
    }

    private Instant getNowInstant() {
        return Instant.now().atZone(ZoneId.of("America/Sao Paulo")).toInstant();
    }
}
