package org.muganga.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import org.muganga.R;

import java.util.ArrayList;

public class LocationsActivity extends AppCompatActivity {

    static ArrayList<String> places;
    static ArrayAdapter<String> arrayAdapter;
    static ArrayList<LatLng> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        ///from percival

        ListView listView = (ListView) findViewById(R.id.listView);

        places = new ArrayList<>();

        places.add("Add a new place...");


        locations = new ArrayList<>();

        locations.add(new LatLng(0, 0));


        Log.e("Places", places.toString());

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, places);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
Log.e("Locatios", "clicked at position: " + position + "and id:  " + id);
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                i.putExtra("locationInfo", position);
                startActivity(i);

            }

        });

    }

}
