package org.muganga.activities;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.muganga.Logs.Logger;
import org.muganga.R;
import org.muganga.utilities.LocationHelper;

public class UserLocationActivity extends AppCompatActivity implements LocationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);
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

        //for location
         Location location= LocationHelper.getLastKnownLocation(this);
        if (location!=null){
        Logger.longToast("Location found " + location.getLatitude()+ " ; " + location.getLongitude());
        }
        else {
            Logger.longToast("No Location found");

        }
    }

    @Override
    protected void onResume() {
       super.onResume();
        LocationHelper.updateLocation(this,this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Logger.debug(String.valueOf(location.getLatitude()));
        Logger.debug(String.valueOf(location.getLongitude()));


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
