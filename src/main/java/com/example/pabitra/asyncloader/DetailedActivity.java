package com.example.pabitra.asyncloader;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailedActivity extends AppCompatActivity {

    private String mag;
    private String place;
    private String date;

    private TextView Mag;
    private TextView Place;
    private TextView Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        Earthquake earthquake = (Earthquake)getIntent().getSerializableExtra("event");
        mag = earthquake.getMagnitude();
        place = earthquake.getPlace();
        date = earthquake.getDate();

        Mag = (TextView)findViewById(R.id.magnitude_details);
        Place = (TextView)findViewById(R.id.place_details);
        Date = (TextView)findViewById(R.id.date_details);

        Mag.setText("Magnitude: " + mag);
        Place.setText("Place: " + place);
        Date.setText("Date: " + date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem menuItem = menu.findItem(R.id.share);
        ShareActionProvider provider = (ShareActionProvider)MenuItemCompat.getActionProvider(menuItem);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shareIntent.putExtra(Intent.EXTRA_TEXT, Mag.getText().toString() + "\n" + Date.getText().toString());
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Earthquake Report of " + place);

        provider.setShareIntent(shareIntent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
