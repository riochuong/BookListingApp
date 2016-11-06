package booklistingapp.jd.com.booklistingapp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Googlebooks Api helpers go construct
 */

public class GoogleBooksHelper {

    private static final String SEARCH_QUERY_PRE_FIX
            = "https://www.googleapis.com/books/v1/volumes?q=";

    private static final String ITEMS_KEY = "items";
    private static final String VOLUME_INFO_KEY = "volumeInfo";
    private static final String TITLE_INFO_KEY = "title";
    private static final String SUBTITLE_INFO_KEY = "subtitle";
    private static final String AUTHORS_INFO_KEY = "authors";
    private static final String PUBLISHER_INFO_KEY = "publisher";
    private static final String DESCRIPTION_INFO_KEY = "description";
    private static final String INDUSTRY_IDENTIFIERS_KEY = "industryIdentifiers";
    private static final String TYPE_ISBN_KEY = "type";
    private static final String IDENTIFIER_ISBN_KEY = "identifier";
    private static final String PUBLISHED_DATE_KEY = "publishedDate";
    private static final String NA_KEY = "N/A";


    public static URL buildSearhCmd(String term) throws MalformedURLException {
        String searchTerm =  SEARCH_QUERY_PRE_FIX + term.trim();
        // need to replace spaces with special character.
        searchTerm = searchTerm.replace(" ",AppConsts.SPACE_REPLACE_CHAR);
        Uri uri = Uri.parse(searchTerm);
        return new URL(uri.toString());
    }

    public static List<VolumeBook> parseJsonResponeFromgGoogleBooks(String response)
            throws JSONException {

        List<VolumeBook> bookList = new ArrayList<>();

        if (response == null){
            return bookList;
        }


        JSONObject jsonResponse = new JSONObject(response);
        // get the array of book items;
        JSONArray jsonBookItems = jsonResponse.getJSONArray(ITEMS_KEY);

        // go through each book and parse it
        for (int i = 0; i < jsonBookItems.length(); i++) {
            VolumeBook resBook = parseBookFromJson(jsonBookItems.getJSONObject(i));
            if (resBook != null) {
                Log.d(AppConsts.TAG, "Add book to list " + resBook.getTitle());
                bookList.add(resBook);
            }
        }

        return bookList;
    }


    public static VolumeBook parseBookFromJson(JSONObject bookItem) throws JSONException {

        JSONObject volumeInfo = bookItem.getJSONObject(VOLUME_INFO_KEY);
        JSONArray authorsArray = volumeInfo.getJSONArray(AUTHORS_INFO_KEY);
        List<String> authorList = new ArrayList<>();
        String title = volumeInfo.has(TITLE_INFO_KEY) ?
                volumeInfo.getString(TITLE_INFO_KEY) : NA_KEY;

        String subtitle = (volumeInfo.has(SUBTITLE_INFO_KEY)) ?
                volumeInfo.getString(SUBTITLE_INFO_KEY) : NA_KEY;
        String publisher = volumeInfo.has(PUBLISHER_INFO_KEY) ?
                    volumeInfo.getString(PUBLISHER_INFO_KEY): NA_KEY;
        String description = volumeInfo.has(DESCRIPTION_INFO_KEY) ?
                    volumeInfo.getString(DESCRIPTION_INFO_KEY) : NA_KEY;
        String publishedDate = volumeInfo.has(PUBLISHED_DATE_KEY) ?
                volumeInfo.getString(PUBLISHED_DATE_KEY):NA_KEY;
        HashMap<String, String> industryISBNS =
                parseISBN(
                        (volumeInfo.has(INDUSTRY_IDENTIFIERS_KEY) ?
                                volumeInfo.getJSONArray(INDUSTRY_IDENTIFIERS_KEY) : null));
        // put authors to list ;
        for (int i = 0; i < authorsArray.length(); i++) {
            authorList.add(authorsArray.getString(i));
        }

        // return the new book
        return new VolumeBook(
                title,
                subtitle,
                authorList,
                description,
                publisher,
                industryISBNS,
                publishedDate
        );

    }

    /**
     * parse all ISBN of the books so it can be retrieve later on
     *
     * @param isbns
     * @return
     * @throws JSONException
     */
    private static HashMap<String, String> parseISBN(JSONArray isbns) throws JSONException {
        HashMap<String, String> isbnMap = new HashMap<>();
        if (isbns != null) {
            for (int i = 0; i < isbns.length(); i++) {
                isbnMap.put(isbns.getJSONObject(i).getString(TYPE_ISBN_KEY),
                        isbns.getJSONObject(i).getString(IDENTIFIER_ISBN_KEY));

            }
        }
        return isbnMap;
    }


}
