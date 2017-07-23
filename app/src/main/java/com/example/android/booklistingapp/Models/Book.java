package com.example.android.booklistingapp.Models;

public class Book {
    String title;
    String author;
    String publishedDate;

    public Book(String title, String author, String publishedDate) {
        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishedDate() {
        return publishedDate;
    }
}
