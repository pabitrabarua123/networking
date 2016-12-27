package com.example.pabitra.asyncloader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by PABITRA on 11/13/2016.
 */
public class myAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Earthquake>> {

    private String mUrl;

    public myAsyncTaskLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Earthquake> loadInBackground() {

        Log.i("TEST", "......loadInBackground......");

        URL url = null;
        try {
            url = new URL(mUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (url == null){
            return null;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String jsonResponse = null;

        try {
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            Log.i("TEST", "Response Code -> " + Integer.toString(responseCode));
            if (responseCode == 200) {
                inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                StringBuilder builder = new StringBuilder();
                String line = reader.readLine();
                while (line != null){
                    builder.append(line);
                    line = reader.readLine();
                }

                jsonResponse = builder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            return getData(jsonResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Earthquake> getData(String jsresponse) throws JSONException {

        if (jsresponse == null){
          return null;
        }

        JSONObject earthquakeData = new JSONObject(jsresponse);
        JSONArray earthquakeArray = earthquakeData.getJSONArray("features");
        ArrayList<Earthquake> EarthquakeList= new ArrayList<>();

        for (int i = 0; i < earthquakeArray.length(); i++) {
            JSONObject EearthquakeEvent = earthquakeArray.getJSONObject(i);
            JSONObject Event = EearthquakeEvent.getJSONObject("properties");
            double magnitude = Event.getDouble("mag");
            String magnitudeString = Double.toString(magnitude);
            long date = Event.getLong("time");
            Date date1 = new Date(date);
            SimpleDateFormat  simpleDateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
            String dateString = simpleDateFormat.format(date1);
            String place = Event.getString("place");

            Earthquake earthquake = new Earthquake(dateString, magnitudeString, place);
            EarthquakeList.add(earthquake);
        }

        return EarthquakeList;
    }
}
