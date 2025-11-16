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

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DownloadService {

    private final DownloadMapper downloadMapper;
    private final DownloadRepository downloadRepository;
    private final UserService userService;
    private final BookService bookService;
    private final StorageService storageService;


    @Transactional
    public DownloadableFile downloadPdf(Long bookId, UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        Book book = bookService.getBookById(bookId);

        DownloadableFile file = storageService.getPdf(book.getPdfFile().getPdfKey());
        file.setFileName(book.getPdfFile().getFilename());

        if (downloadRepository.findByBookIdAndUserId(bookId, user.getId()).isEmpty()) {
            downloadRepository.save(new Download(null, LocalDate.now(), book, user));
        }

        return file;
    }

    @Transactional(readOnly = true)
    public List<DownloadHistoricResponse> getUserHistoric(UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());

        return user
                .getDownloads()
                .stream()
                .map(downloadMapper::toDownloadHistoric)
                .toList();
    }
}
