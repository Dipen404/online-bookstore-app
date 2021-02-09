package com.dipen.bookstore.books.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.dipen.bookstore.books.entity.Book;
import com.dipen.bookstore.books.entity.Category;

import java.util.List;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
    List<Book> findByCategoryId(int id);
    Page<Book> findByCategoryId(int id, Pageable pageable);
}
