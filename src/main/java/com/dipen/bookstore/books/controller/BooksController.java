package com.dipen.bookstore.books.controller;

import com.dipen.bookstore.books.entity.Book;
import com.dipen.bookstore.books.service.BookService;
import com.dipen.bookstore.books.service.impl.BookServiceImpl;
import com.dipen.bookstore.books.service.impl.CategoryServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/books")
public class BooksController {
    @Autowired
    private CategoryServiceImpl categoryService;
    @Autowired
    private BookServiceImpl bookService;

    @GetMapping("/{id}")
    public String categoryBook(@PathVariable("id") final int id, @RequestParam( name = "pageno", defaultValue = "0") int pageNo, Model model) {
        Page<Book> pagedResult = bookService.getAllPaginatedBooksByCategoryId(id, pageNo, 6);
        model.addAttribute("categoryList", categoryService.getAllCategory());
        model.addAttribute("selectedCategory", id);
        model.addAttribute("bookList", pagedResult.toList());
        model.addAttribute("totalPages", pagedResult.getTotalPages());
        return "index";
    }
}
