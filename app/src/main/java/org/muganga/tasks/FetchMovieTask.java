package org.muganga.tasks;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;

import org.muganga.Callbacks.ItemLoadedListener;
import org.muganga.data.Movie;
import org.muganga.VolleySingleton;
import org.muganga.json.Requestor;
import org.muganga.json.TrailerParser;
import org.muganga.utilities.EndPoints;
import org.muganga.utilities.LogHelper;

import org.json.JSONObject;

/**
 * Created by itl on 31/07/2015.
 */
public class FetchMovieTask extends AsyncTask<String, Void, Movie> {

    public JSONObject response;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ItemLoadedListener myComponent;



    public FetchMovieTask(ItemLoadedListener myComponent) {
        this.myComponent = myComponent;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }


    @Override
    protected Movie doInBackground(String... params) {

        String title=params[0];


        String url= EndPoints.getRequestMovieUrl(title);
      // String url="http://www.myapifilms.com/imdb?title=thehelp&trailer=1";
        try {


          response = Requestor.requestMoviesJSON(requestQueue, url);
        } catch (Exception e) {
            LogHelper.log("Volley error " + e.getMessage());
        }

        LogHelper.log("doInBackground: URL== " + url);
        LogHelper.log("doInBackground: response== " + response);
        Movie  parsedMovie= null;
        try {
            parsedMovie = TrailerParser.parseSearchMovie(response);
        } catch (Exception e) {
            LogHelper.log("Parse error " + e.getMessage());
        }

        LogHelper.log("doInBackground: return== " + parsedMovie);

        return parsedMovie;

    }

    @Override
    protected void onPostExecute(Movie movie) {
        if (myComponent != null) {
            myComponent.onMovieLoded(movie);
        }


    }

}