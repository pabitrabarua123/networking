package com.example.pabitra.asyncloader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Earthquake>>, AdapterView.OnItemClickListener{

    private static final int LOADER_ID = 1;
    private ListView listView;
    private TextView MultipleText;
    private ProgressBar loadingIndicator;
    private ArrayAdapter<Earthquake> mAdapter;
    private ArrayList<Earthquake> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("TEST", "......onCreate........");

        listView = (ListView)findViewById(R.id.listView);
        MultipleText = (TextView)findViewById(R.id.multiple_text);
        loadingIndicator = (ProgressBar)findViewById(R.id.loading_indicator);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int id, Bundle args) {

        Log.i("TEST", ".....onCreateLoader.....");

        String EARTHQUAKE_BASE_URl = "http://earthquake.usgs.gov/fdsnws/event/1/query?";
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String minmagnitude = pref.getString("mag", "3");
        String limit = pref.getString("limit", "10");

        Uri uri = Uri.parse(EARTHQUAKE_BASE_URl).buildUpon()
                .appendQueryParameter("format", "geojson")
                .appendQueryParameter("minmagnitude", minmagnitude)
                .appendQueryParameter("limit", limit).build();

        String url = uri.toString();

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() ) {
           return new myAsyncTaskLoader(this, url);
        } else {
           MultipleText.setText(R.string.no_internet);
            loadingIndicator.setVisibility(View.INVISIBLE);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> list) {

        Log.i("TEST", "......onLoadFinished......");

        arrayList = list;
        loadingIndicator.setVisibility(View.GONE);
        if (arrayList != null && !arrayList.isEmpty()) {
            mAdapter = new EarthquakeAdapter(this, arrayList);
            listView.setAdapter(mAdapter);
        }else {
            MultipleText.setText(R.string.no_data_found);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.i("TEST", "......onLoaderReset.....");
        mAdapter.clear();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Earthquake earthquake = arrayList.get(i);
        Intent intent = new Intent(this, DetailedActivity.class);
        intent.putExtra("event", earthquake);
        startActivity(intent);
    }
}
