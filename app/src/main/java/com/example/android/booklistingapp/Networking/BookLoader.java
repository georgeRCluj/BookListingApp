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
    protected void onForceLoad() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (urls.length < 1 || urls[0] == null) {
            return null;
        }
        // List<Book> books = the list of books fetched from the API
        return null;
    }
}
