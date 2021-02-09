package com.dipen.bookstore.books.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CartDto {

    private int bookId;
    private String title;
    private String author;
    private String imageUrl;
    private double price;
    private int quantity;
}
