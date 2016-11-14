package booklistingapp.jd.com.booklistingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by chuondao on 11/5/16.
 */

public class HttpConnectionHelper {

    private static int READ_TIMEOUT = 10000; // in ms
    private static int CONNECT_TIMEOUT = 15000; // in ms

    /**
     * Open http connection and make a request
     *
     * @param url - url of the data resources
     * @return the string response from the server (should be  json in our case
     * @throws IOException
     */
    public static String makeHttpRequest(URL url, String methodType) throws IOException {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String response = null;
        try {
            if (url != null) {
                Log.d(AppConsts.TAG, "Making HTTP Request with URL:" + url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(methodType);
                urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
                urlConnection.setReadTimeout(READ_TIMEOUT);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                response = readResponseFromInputStream(inputStream);
                Log.d(AppConsts.TAG, "HTTP response" + response);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return response;
    }

    /**
     * read response from server
     *
     * @param is
     * @return
     */
    private static String readResponseFromInputStream(InputStream is) {
        BufferedReader breader = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line = null;

        try {
            //start read and append to string buffer
            do {

                line = breader.readLine();
                sb.append(line);

            } while (line != null);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();

    }

    /**
     * Check if we are connecting to network
     * need to be called on separate thread
     *
     * @param ctx
     * @return true
     */
    public static boolean isConnectToInternet(Context ctx) {
        ConnectivityManager conMan =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();

        return (netInfo != null) && (netInfo.isConnected());
    }

}
