package com.dipen.bookstore.books.service.impl;

import com.dipen.bookstore.books.dto.BookDto;
import com.dipen.bookstore.books.entity.Book;
import com.dipen.bookstore.books.repository.BookRepository;
import com.dipen.bookstore.books.repository.CategoryRepository;
import com.dipen.bookstore.books.service.BookService;
import com.dipen.bookstore.utility.FileUploader;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FileUploader fileUploader;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Book createBook(BookDto bookDto) throws IOException {

        //1. upload book
        String imagePath = fileUploader.upload(bookDto.getImage());

        Book book = modelMapper.map(bookDto, Book.class);
        book.setImageUrl(imagePath);
        book.setCategory(categoryRepository.getOne(bookDto.getCategoryId()));

        return bookRepository.save(book);

    }

    @Override
    public Iterable<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Page<Book> getPaginatedBook(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return bookRepository.findAll(paging);
    }

    @Override
    public List<Book> getAllBooksByCategoryId(int categoryId) {
        return bookRepository.findByCategoryId(categoryId);
    }

    public Page<Book> getAllPaginatedBooksByCategoryId(int categortId, int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return bookRepository.findByCategoryId(categortId, paging);
    }


    public void storeImage(MultipartFile file) {
        String UPLOADED_FOLDER = "F://temp//";
    }
}
