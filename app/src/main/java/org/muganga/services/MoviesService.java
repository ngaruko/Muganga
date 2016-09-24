package org.muganga.services;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import org.muganga.data.MoviesContract;
import org.muganga.json.JSonParser;
import org.muganga.utilities.DialogClass;

import java.util.ArrayList;

public class MoviesService extends IntentService {

    public static final String BROADCAST_ACTION_STATE_CHANGE
            = "org.muganga.intent.action.STATE_CHANGE";
    public static final String EXTRA_REFRESHING
            = "org.muganga.intent.extra.REFRESHING";
    private static final String TAG = "MoviesService";

    public MoviesService() {
        super(TAG);


    }


    @Override
    protected void onHandleIntent(Intent intent) {


        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            Log.w(TAG, "Not online, not refreshing.");

            startActivity(new Intent(this, DialogClass.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


            return;
        }

        sendStickyBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true));

        // Don't even inspect the intent, we only do one thing, and that's fetch content.
        ArrayList<ContentProviderOperation> cpoTheaters = new ArrayList<ContentProviderOperation>();
        ArrayList<ContentProviderOperation> cpoTop = new ArrayList<ContentProviderOperation>();
        ArrayList<ContentProviderOperation> cpoBottom = new ArrayList<ContentProviderOperation>();
        ArrayList<ContentProviderOperation> cpoComing = new ArrayList<ContentProviderOperation>();


        Uri theatersUri = MoviesContract.InTheater.buildDirUri();
        Uri topMoviesUri = MoviesContract.TopMovies.buildDirUri();
        Uri bottomMoviesUri = MoviesContract.BottomMovies.buildDirUri();
        Uri comingSoonUri = MoviesContract.ComingSoon.buildDirUri();


        // Delete all items
        cpoTheaters.add(ContentProviderOperation.newDelete(theatersUri).build());
        cpoTop.add(ContentProviderOperation.newDelete(topMoviesUri).build());
        cpoBottom.add(ContentProviderOperation.newDelete(bottomMoviesUri).build());
        cpoComing.add(ContentProviderOperation.newDelete(comingSoonUri).build());


              new JSonParser(this).parseMaladiesData(cpoTheaters, theatersUri);

       new JSonParser(this).parseEntitiesData(cpoTop, topMoviesUri);



        sendStickyBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
    }


}
