package com.projeto_integrado_biblioteca.domains.upload.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class UploadService {
    private static final String CREDENTIALS_FILE_PATH = getPathToGoogleCredentials();
    private static final GsonFactory GSON_FACTORY = GsonFactory.getDefaultInstance();

    @Value("${folder.cover.id}")
    private String pdfFolderId;

    private static String getPathToGoogleCredentials() {
        String currentDir = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDir, "cred.json");

        return filePath.toString();
    }

    public void uploadBookCover(File file) throws GeneralSecurityException, IOException {
        try {
            Drive drive = createDriveService();

            com.google.api.services.drive.model.File fileMetaData = new com.google.api.services.drive.model.File();
            fileMetaData.setName(file.getName());
            fileMetaData.setParents(Collections.singletonList(pdfFolderId));

            FileContent mediaContent = new FileContent("image/jpeg", file);

            com.google.api.services.drive.model.File uploadedFile = drive
                    .files()
                    .create(fileMetaData, mediaContent)
                    .setFields("id").execute();

            System.out.println(uploadedFile.getId());
            System.out.println(uploadedFile.getSize());
            file.delete();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Drive createDriveService() throws GeneralSecurityException, IOException {
        GoogleCredentials credentials = GoogleCredentials
                .getApplicationDefault()
                .createScoped(List.of(DriveScopes.DRIVE_FILE));
        HttpRequestInitializer httpRequestInitializer = new HttpCredentialsAdapter(credentials);

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GSON_FACTORY,
                httpRequestInitializer
        ).build();
    }
}
