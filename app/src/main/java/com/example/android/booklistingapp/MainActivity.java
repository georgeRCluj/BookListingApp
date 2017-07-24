package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.booklistingapp.Adapters.BooksListAdapter;
import com.example.android.booklistingapp.Models.Book;
import com.example.android.booklistingapp.Networking.BookLoader;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.booklistingapp.UtilsLibraries.Constants.API_KEY_CONNECTION;
import static com.example.android.booklistingapp.UtilsLibraries.Constants.BOOKS_LISTING_URL;
import static com.example.android.booklistingapp.UtilsLibraries.Constants.BOOKS_LOADER_ID;
import static com.example.android.booklistingapp.Networking.NetworkUtils.getStringFromResources;
import static com.example.android.booklistingapp.Networking.NetworkUtils.isConnectedToNet;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    private View loadingIndicator;
    private TextView emptyTextView;
    private ListView listView;
    private Button noNetRetryButton;
    private Button searchButton;
    private LinearLayout searchLayout;
    private BooksListAdapter booksAdapter;
    private EditText searchEditText;
    private LoaderManager loaderManager;
    private Boolean isFirstSearch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUiComponents();
        initializeBooksList();
        checkConnectionToNetAndProceed();
        setListenerOnSearchButton();
    }

    private void initializeUiComponents() {
        setContentView(R.layout.activity_main);
        loadingIndicator = findViewById(R.id.loadingSpinnerId);
        emptyTextView = (TextView) findViewById(R.id.emptyViewId);
        listView = (ListView) findViewById(R.id.baseListId);
        listView.setEmptyView(emptyTextView);
        noNetRetryButton = (Button) findViewById(R.id.retryButtonId);
        searchLayout = (LinearLayout) findViewById(R.id.searchBookLayoutId);
        searchButton = (Button) findViewById(R.id.searchButtonId);
        searchEditText = (EditText) findViewById(R.id.searchEditTextId);
    }

    private void initializeBooksList() {
        final List<Book> books = new ArrayList<>();
        booksAdapter = new BooksListAdapter(this, books);
        listView.setAdapter(booksAdapter);
    }

    private void checkConnectionToNetAndProceed() {
        if (isConnectedToNet(this)) {
            emptyTextView.setText(getStringFromResources(this, R.string.empty_list_message));
            noNetRetryButton.setVisibility(View.GONE);
            searchLayout.setVisibility(View.VISIBLE);
        } else {
            searchLayout.setVisibility(View.GONE);
            loadingIndicator.setVisibility(View.GONE);
            emptyTextView.setText(getStringFromResources(this, R.string.no_internet_connection_message));
            noNetRetryButton.setVisibility(View.VISIBLE);
            setListenerOnRetryButton();
        }
    }

    private void setListenerOnRetryButton() {
        noNetRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnectionToNetAndProceed();
            }
        });
    }

    private void setListenerOnSearchButton() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnectedToNet(getApplicationContext())) {
                    loaderManager = getLoaderManager();
                    if (isFirstSearch) {
                        loaderManager.initLoader(BOOKS_LOADER_ID, null, MainActivity.this);
                        isFirstSearch = false;
                    } else {
                        loaderManager.restartLoader(BOOKS_LOADER_ID, null, MainActivity.this);
                    }
                } else {
                    checkConnectionToNetAndProceed();
                }
            }
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        Log.e("LCREATE", id + ";");
        String final_url = BOOKS_LISTING_URL + searchEditText.getText().toString().trim() + API_KEY_CONNECTION + getStringFromResources(MainActivity.this, R.string.BookListing_API_Key);
        emptyTextView.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.VISIBLE);
        return new BookLoader(this, final_url);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        loadingIndicator.setVisibility(View.GONE);
        booksAdapter.clear();
        if (books != null && !books.isEmpty()) {
            booksAdapter.addAll(books);
        } else {
            emptyTextView.setText(getStringFromResources(this, R.string.empty_list_message));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        booksAdapter.clear();
    }
}
