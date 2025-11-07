package com.projeto_integrado_biblioteca.domains.download;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/downloads")
@RequiredArgsConstructor
public class DownloadController {
    private final DownloadService downloadService;

    @GetMapping(value = "/{id}")
    //@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Resource> download(
            @PathVariable Long id
    ) {
        DownloadableFile downloadableFile = downloadService.download(id);


        InputStreamResource resource = new InputStreamResource(downloadableFile.getInputStream());


        ContentDisposition disposition = ContentDisposition
                .attachment()
                .filename(downloadableFile.getFileName(), StandardCharsets.UTF_8)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(disposition);
        headers.setContentType(MediaType.parseMediaType(downloadableFile.getContentType()));
        headers.setContentLength(downloadableFile.getContentLength());

        return ResponseEntity.ok().headers(headers).body(resource);
    }
}
