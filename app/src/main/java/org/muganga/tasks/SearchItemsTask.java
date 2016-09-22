package org.muganga.tasks;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;

import org.muganga.Callbacks.ItemsFoundListener;
import org.muganga.VolleySingleton;
import org.muganga.data.MoviesContract;
import org.muganga.json.JSonParser;
import org.muganga.json.Requestor;
import org.muganga.utilities.EndPoints;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchItemsTask extends AsyncTask<String, Void, Void> {

    public JSONArray response;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ItemsFoundListener myComponent;
    private Context mContext;
    private JSONObject mResponse;


    public SearchItemsTask(ItemsFoundListener myComponent, Context context) {
        this.myComponent = myComponent;
        this.mContext = context;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }


    @Override
    protected Void doInBackground(String... params) {

        String title = params[0];
        String limit = params[1];

        ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();

        Uri uri = MoviesContract.FoundMovies.buildDirUri();


        // Delete all items
        cpo.add(ContentProviderOperation.newDelete(uri).build());


        mResponse = null;

        try {
            mResponse = Requestor.requestMoviesJSON(requestQueue, EndPoints.getRequestUrlFoundMovies(title, limit));

            if (mResponse != null && mResponse.length() > 0)
                new JSonParser(mContext).parseAndSaveTheatersMovies(cpo, uri, mResponse,"");

        } catch (Exception e) {

            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        if (myComponent != null) {

            myComponent.onMoviesFound(mResponse);
        }


    }

}