package com.dipen.bookstore.books.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_book")
@Data
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookId;

    private String title;
    private String author;
    private String publisher;
    private Date publicationDate;
    private String language;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cat_id")
    private Category category;

    private Double price;
    private boolean active;
    @Lob
    private String description;
    private int inStockNumber;
    private String imageUrl;

    public Book(String title, String author, String publisher, Date publicationDate,
                String language, Double price, boolean active, String description, int inStockNumber, String imageUrl) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.language = language;
        this.price = price;
        this.active = active;
        this.description = description;
        this.inStockNumber = inStockNumber;
    }
}
