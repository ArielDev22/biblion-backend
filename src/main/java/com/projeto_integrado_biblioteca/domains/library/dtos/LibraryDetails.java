package com.projeto_integrado_biblioteca.domains.library.dtos;

import java.util.List;

public record LibraryDetails(
        Integer numberOfBooks,
        List<LibraryBookDetails> books
) {
}
