package com.example.android.booklistingapp.Networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.booklistingapp.Models.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.booklistingapp.UtilsLibraries.Constants.CONNECTION_TIMEOUT;
import static com.example.android.booklistingapp.UtilsLibraries.Constants.LOG_TAG;
import static com.example.android.booklistingapp.UtilsLibraries.Constants.READ_TIMEOUT;

public class NetworkUtils {

    public static boolean isConnectedToNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnected());
        return isConnected;
    }

    public static String getStringFromResources(Context context, int resourceId) {
        return context.getResources().getString(resourceId);
    }

    public static List<Book> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Book> books = extractFeatureFromJson(jsonResponse);
        return books;
    }

    public static List<Book> extractFeatureFromJson(String requestUrl) {
        if (TextUtils.isEmpty(requestUrl)) {
            return null;
        }
        List<Book> books = new ArrayList<>();

        try {
            JSONObject jsonObj = new JSONObject(requestUrl);
            if (jsonObj.has("items")) {
                JSONArray items = jsonObj.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject currentBook = items.getJSONObject(i);
                    if (currentBook.has("volumeInfo")) {
                        JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                        if (volumeInfo.has("title")) {
                            String title = volumeInfo.getString("title");
                            String publishedDate = "Published date N/A";
                            if (volumeInfo.has("publishedDate")) {
                                publishedDate = volumeInfo.getString("publishedDate");
                            }
                            String allAuthors = "Author N/A";
                            if (volumeInfo.has("authors")) {
                                allAuthors = "";
                                JSONArray authors = volumeInfo.getJSONArray("authors");
                                for (int j = 0; j < authors.length(); j++) {
                                    String currentAuthor = authors.getString(j);
                                    allAuthors += currentAuthor + "; ";
                                }
                            }
                            books.add(new Book(title, allAuthors, publishedDate));
                        }
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }
        return books;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
