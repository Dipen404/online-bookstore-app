package com.dipen.bookstore.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dipen.bookstore.books.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
