package com.smap.f16.grp12.racketometer.utils.performanceApi;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.smap.f16.grp12.racketometer.R;
import com.smap.f16.grp12.racketometer.models.Session;
import com.smap.f16.grp12.racketometer.utils.ConnectivityHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Performance API taskOpen Weather Map API task to get latest weather information.
 */
public class PerformanceApi extends AsyncTask<Void, Void, List<Session>> {
    private final static String LOG = "PerformanceApi";

    private final String API_URL;
    private final static int MAXIMUM_CHARACTERS = 1000;
    private final static int REQUEST_TIMEOUT = 5000;
    private final static int CONNECTION_TIMEOUT = 10000;

    private final Context context;
    private final SessionsReceivedCallback listener;

    public PerformanceApi(Context context, SessionsReceivedCallback listener) {
        this.context = context;
        this.listener = listener;

        API_URL = context.getString(R.string.performance_api_url);
    }

    @Override
    protected List<Session> doInBackground(Void... params) {
        return getSessions();
    }

    /**
     * Get sessions from the API.
     *
     * @return The received sessions or null.
     */
    private List<Session> getSessions() {
        try {
            String endpoint = API_URL;

            String data = downloadUrl(endpoint);

            return JsonParser.getSessions(data);
        } catch (IOException e) {
            Log.e(LOG, "IO Exception when getting sessions");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Callback method when task has finished.
     *
     * @param sessions The result of the task.
     */
    @Override
    protected void onPostExecute(List<Session> sessions) {
        listener.sessionsReceived(sessions);

        super.onPostExecute(sessions);
    }

    /**
     * Get JSON data from url with HTTP GET request.
     * This method is partially copied from:
     * http://developer.android.com/training/basics/network-ops/connecting.html#download
     *
     * @param urlString The URL to connect to.
     * @return JSON string with received data or null on connection errors.
     * @throws IOException
     */
    private String downloadUrl(String urlString) throws IOException {
        ConnectivityHelper connectivityHelper = new ConnectivityHelper(context);

        if (connectivityHelper.isOnline()){
            InputStream is = null;
            HttpURLConnection conn = null;

            try {
                URL url = new URL(urlString);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(REQUEST_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();
                int response = conn.getResponseCode();

                if (response < 200 || response > 299) {
                    Log.e(LOG, "Bad response from PerformanceAPI. Response code: " + response);
                    return null;
                }

                is = conn.getInputStream();

                return readIt(is);
            } finally {
                if (is != null) {
                    is.close();
                }

                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
        return null;
    }

    /**
     * Read input stream with a maximum of characters.
     * This method is copied from:
     * http://developer.android.com/training/basics/network-ops/connecting.html#download
     *
     * @param stream The stream to read.
     * @return String with read data.
     * @throws IOException
     */
    private static String readIt(InputStream stream) throws IOException {
        Reader reader;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[MAXIMUM_CHARACTERS];
        reader.read(buffer);
        reader.close();

        return new String(buffer);
    }
}
