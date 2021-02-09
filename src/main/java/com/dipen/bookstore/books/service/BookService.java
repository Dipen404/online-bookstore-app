package com.dipen.bookstore.books.service;

import com.dipen.bookstore.books.dto.BookDto;
import com.dipen.bookstore.books.entity.Book;
import com.dipen.bookstore.books.entity.Category;

import java.io.IOException;
import java.util.List;

public interface BookService {
    Book createBook(BookDto bookDto) throws IOException;
    Iterable<Book> getAllBooks();
    List<Book> getAllBooksByCategoryId(int categoryId);
}
