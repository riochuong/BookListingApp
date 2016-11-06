package booklistingapp.jd.com.booklistingapp;

import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookListingActivity extends AppCompatActivity implements
        View.OnClickListener, LoaderManager.LoaderCallbacks<List<VolumeBook>> {

    /* contains search term */
    EditText searchEditText = null;
    Button searchBtn = null;
    ListView textBookResultsListView = null;
    View emptyView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_listing);
        searchBtn = (Button) findViewById(R.id.search_btn);
        searchEditText = (EditText) findViewById(R.id.seach_edit_text);
        searchBtn.setOnClickListener(this);
        emptyView = getLayoutInflater().inflate(R.layout.empty_list_view,null,false);

        setEmptyViewText(getString(R.string.search_info_txt));
        textBookResultsListView = (ListView) findViewById(R.id.book_list_view);
        ((ViewGroup)textBookResultsListView.getParent()).addView(emptyView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        textBookResultsListView.setEmptyView(emptyView);
        textBookResultsListView.setAdapter(
                new VolumeBookArrayAdapter(this, new ArrayList<VolumeBook>())
        );
    }

    /**
     * helper for dynamically set text on empty view
     * @param text
     */
    private void setEmptyViewText(String text)
    {
        if (emptyView != null){
            TextView emptyViewText = (TextView) emptyView.findViewById(R.id.empty_text_view);
            emptyViewText.setText(text);
        }

    }

    /* implements search btn on click*/
    @Override
    public void onClick(View view) {
        // only search button can come here
        /**
         * 1 . get the search term
         * 2 . make the http request to search on GOOGle BOOKS SERVEr
         * 3 . Get the raw resonse JSON and parse it.
         * 4 . Construct BOOKS volume object list to be display
         */

        try {
            String termToSearch = searchEditText.getText().toString();
            final URL urlToSearch = GoogleBooksHelper.buildSearhCmd(termToSearch);

            /**
             *  Execute network request whenever the button is clicked...
             */
            new AsyncTask<URL, String, List<VolumeBook>>() {

                private boolean isNetworkDisconnected = false;

                @Override
                protected void onPostExecute(List<VolumeBook> bookList) {
                    // after receiving list book let's publish it
                    // through array Adapter
                    if (bookList != null && bookList.size() > 0) {
                        textBookResultsListView.setAdapter(
                                new VolumeBookArrayAdapter(BookListingActivity.this, bookList));
                    } else {
                        // show toast indicate we cannot find any book in the list
                        textBookResultsListView.setAdapter(new VolumeBookArrayAdapter(
                                BookListingActivity.this, new ArrayList<VolumeBook>()
                        ));
                        if (isNetworkDisconnected){
                            setEmptyViewText(getString(R.string.network_connection_error));
                            Toast.makeText(BookListingActivity.this,
                                    getString(R.string.network_connection_error),
                                    Toast.LENGTH_LONG).show();
                        }
                        else{

                            setEmptyViewText(getString(R.string.no_found));
                            Toast.makeText(BookListingActivity.this,
                                    getString(R.string.no_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    }


                }

                @Override
                protected List<VolumeBook> doInBackground(URL... url) {
                    String response = null;
                    List<VolumeBook> listBook = null;
                    try {

                        if (!HttpConnectionHelper.isConnectToInternet(BookListingActivity.this)) {
                            isNetworkDisconnected = true;
                            Log.e(AppConsts.TAG,"Device is not connected to the Internet");
                        }

                        if (url.length > 0) {
                            response =
                                    HttpConnectionHelper.makeHttpRequest(url[0], AppConsts.GET_REQUEST_TYPE);
                        }
                        // call parsing method to help parse this ...
                        listBook =
                                GoogleBooksHelper.parseJsonResponeFromgGoogleBooks(response);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(BookListingActivity.this,
                                getString(R.string.ioexception_error_toast),
                                Toast.LENGTH_LONG)
                                .show();
                        Log.e(AppConsts.TAG, "IO exception please check network connection");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(AppConsts.TAG, "Failed to parse the JSON response from server");
                    }
                    return listBook;
                }


            }.execute(urlToSearch);


        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(AppConsts.TAG, " Failed to Get the book list from HTTP request");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(AppConsts.TAG, "IOException from HTTP request");
        }


    }


    @Override
    public Loader<List<VolumeBook>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<VolumeBook>> loader, List<VolumeBook> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<VolumeBook>> loader) {

    }
}
