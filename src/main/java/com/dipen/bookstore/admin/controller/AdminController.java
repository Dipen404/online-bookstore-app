package com.dipen.bookstore.admin.controller;

import com.dipen.bookstore.books.dto.BookDto;
import com.dipen.bookstore.books.service.BookService;
import com.dipen.bookstore.books.service.impl.CategoryServiceImpl;
import com.dipen.bookstore.utility.exception.EmptyFileException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private CategoryServiceImpl categoryService;
    @Autowired
    private BookService bookService;

    @GetMapping(value = "/dashboard")
    public String adminHomepage(Model model,Principal principal) {
        model.addAttribute("email", principal.getName());
        return "admin/index";
    }

    @GetMapping(value = "/add-book")
    public String addBook(Model model,Principal principal) {
        model.addAttribute("email", principal.getName());
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("new_book", new BookDto());
        return "admin/addNewBook";
    }

    @PostMapping(value = "/add-book")
    public String processAddBook(@Valid BookDto bookDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getErrorCount());
            model.addAttribute("new_book", new BookDto());
            return "admin/addNewBook";
        } else {
            try {
                bookService.createBook(bookDto);
            } catch (IOException | EmptyFileException e) {
               log.error(e.getMessage());
            }
        }

        return "redirect:/admin/all-books";
    }

    @GetMapping(value = "/all-books")
    public String listAllBooks(Model model, Principal principal) {
        model.addAttribute("email", principal.getName());
        return "admin/listBooks";
    }
}
