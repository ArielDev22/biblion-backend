package com.projeto_integrado_biblioteca.domains.download;

import com.projeto_integrado_biblioteca.domains.book.Book;
import com.projeto_integrado_biblioteca.domains.book.BookService;
import com.projeto_integrado_biblioteca.domains.storage.StorageService;
import com.projeto_integrado_biblioteca.domains.user.User;
import com.projeto_integrado_biblioteca.domains.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DownloadService {

    private final DownloadMapper downloadMapper;
    private final DownloadRepository downloadRepository;
    private final UserService userService;
    private final BookService bookService;
    private final StorageService storageService;


    @Transactional
    public DownloadableFile registerDownloadAndReturnPdf(Long bookId, UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        Book book = bookService.getBookById(bookId);

        DownloadableFile file = storageService.getPdfForDownload(book.getPdfFile());

        var optDownload = downloadRepository.findByBookIdAndUserId(bookId, user.getId());

        Download download = optDownload
                .map(d -> {
                    d.updateDownloadDate();
                    return d;
                })
                .orElseGet(() -> new Download(book, user));

        downloadRepository.save(download);

        return file;
    }

    @Transactional(readOnly = true)
    public List<DownloadHistoricResponse> getUserHistoric(UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());

        Map<UUID, Download> userDownloads = downloadRepository.findAllByUserId(user.getId())
                .stream()
                .collect(Collectors.toMap(Download::getId, d -> d));


        List<DownloadHistoricResponse> downloadHistoric = userDownloads
                .values()
                .stream()
                .map(downloadMapper::toDownloadHistoric)
                .toList();

        for (DownloadHistoricResponse d : downloadHistoric) {
            var download = userDownloads.get(d.getId());
            d.setImageURL(storageService.getCoverURL(download.getBook().getImageKey()));
        }

        return downloadHistoric;
    }
}
