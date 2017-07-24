package com.example.android.booklistingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.booklistingapp.Models.Book;
import com.example.android.booklistingapp.R;

import java.util.List;

public class BooksListAdapter extends ArrayAdapter<Book> {
    private TextView titleView;
    private TextView authorView;
    private TextView publishedDateView;
    private Book currentBook;
    private View listItemView;

    public BooksListAdapter(Context context, List<Book> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflateList(position, convertView, parent);
        initializeUiComponents();
        setTextsOnVariables();
        return listItemView;
    }

    private void initializeUiComponents() {
        titleView = (TextView) listItemView.findViewById(R.id.bookTitleId);
        authorView = (TextView) listItemView.findViewById(R.id.bookAuthorId);
        publishedDateView = (TextView) listItemView.findViewById(R.id.bookPublishedDateId);
    }

    private void inflateList(int position,  View convertView, ViewGroup parent) {
        currentBook = getItem(position);
        // Check if the existing view is being reused, otherwise inflate the view
        listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
    }

    private void setTextsOnVariables(){
        titleView.setText(currentBook.getTitle());
        authorView.setText(currentBook.getAuthors());
        publishedDateView.setText(currentBook.getPublishedDate());
    }
}
