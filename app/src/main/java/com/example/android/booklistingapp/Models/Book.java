package com.example.android.booklistingapp.Models;

public class Book {
    String title;
    String authors;
    String publishedDate;

    public Book(String title, String authors, String publishedDate) {
        this.title = title;
        this.authors = authors;
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getPublishedDate() {
        return publishedDate;
    }
}
