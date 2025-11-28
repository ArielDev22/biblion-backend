package com.projeto_integrado_biblioteca.domains.book;

import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class BookSpec {
    protected static Specification<Book> hasTitle(String title) {
        return ((root, query, builder) -> {
            if (Objects.nonNull(title)) {
                return builder.like(builder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
            }
            return builder.conjunction();
        });
    }

    protected static Specification<Book> findUsingParams(BookQueryParams params) {
        return hasTitle(params.getTitle());
    }
}
