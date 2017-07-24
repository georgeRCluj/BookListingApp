package com.example.android.booklistingapp.Networking;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.booklistingapp.Models.Book;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    String[] urls;

    public BookLoader(Context context, String... urls ) {
        super(context);
        this.urls = urls;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (urls.length < 1 || urls[0] == null) {
            return null;
        }
        List<Book> books = NetworkUtils.fetchBookData(urls[0]);
        return books;
    }
}
