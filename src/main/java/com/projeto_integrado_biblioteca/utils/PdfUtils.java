package com.projeto_integrado_biblioteca.utils;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class PdfUtils {
    public int getNumberOfPages(MultipartFile pdf) {
        try {
            PDDocument document = Loader.loadPDF(pdf.getBytes());

            int pages = document.getNumberOfPages();

            System.out.println("PAGINAS: " + pages);

            return pages;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
