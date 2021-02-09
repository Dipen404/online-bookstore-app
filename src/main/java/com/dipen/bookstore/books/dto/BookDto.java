package com.dipen.bookstore.books.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@NoArgsConstructor
public class BookDto {

    private Long bookId;
    @NotEmpty(message = "Title cannot be empty")
    @NotBlank(message = "Title is mandatory")
    @NotNull
    private String title;
    @NotEmpty(message = "Author cannot be empty")
    @NotBlank(message = "Author name is mandatory")
    private String author;
    @NotEmpty(message = "Publisher cannot be empty")
    @NotBlank(message = "Publisher is mandatory")
    private String publisher;

    @NotEmpty(message = "Publication Date cannot be empty")
    @Past(message = "Publication date should be less than current date!!")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date publicationDate;
    private String language;
    private int categoryId;

    @NotEmpty(message = "Price cannot be empty")
    @Min(value = 1, message = "Please insert valid price for the book")
    private Double price;
    private boolean active = true;
    private String description;

    @NotEmpty(message = "Stock number cannot be empty")
    @Min(value = 1, message = "Please insert valid stock number")
    private int inStockNumber;

    @NotNull(message = "Image cannot be null")
    @NotEmpty(message = "Image cannot be empty")
    private MultipartFile image;
}
