package com.projeto_integrado_biblioteca.domains.book;

import lombok.Getter;

@Getter
public enum BookType {
    FREE(1L),
    LOAN(2L);

    private Long id;

    BookType(Long id) {
        this.id = id;
    }

    public static BookType getBookOfId(long id){
        for (BookType bt : BookType.values()){
            if (bt.getId() == id){
                return bt;
            }
        }
        return null;
    }
}
