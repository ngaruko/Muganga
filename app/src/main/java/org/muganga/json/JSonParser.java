package org.muganga.json;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.muganga.MainApplication;
import org.muganga.VolleySingleton;
import org.muganga.data.MoviesContract;
import org.muganga.utilities.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.muganga.data.MoviesContract.CONTENT_AUTHORITY;
import static org.muganga.data.MoviesContract.MoviesColumns.COLUMN_TITLE;
import static org.muganga.utilities.Keys.InTheatersEndpoint.KEY_GENRES;
import static org.muganga.utilities.Keys.InTheatersEndpoint.KEY_ID;
import static org.muganga.utilities.Keys.InTheatersEndpoint.KEY_PLOT;
import static org.muganga.utilities.Keys.InTheatersEndpoint.KEY_RATINGS;
import static org.muganga.utilities.Keys.InTheatersEndpoint.KEY_RELEASE_DATE;
import static org.muganga.utilities.Keys.InTheatersEndpoint.KEY_RUNTIME;
import static org.muganga.utilities.Keys.InTheatersEndpoint.KEY_TITLE;
import static org.muganga.utilities.Keys.InTheatersEndpoint.KEY_URLPOSTER;

/**
 * Created by itl on 28/07/2015.
 */
public class JSonParser {


    public Context context;

    public VolleySingleton volleySingleton;
    public RequestQueue requestQueue;
    private JSONObject currentCondition;


    public JSonParser(Context context) {
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        this.context = context;
    }

    private static Double getRating(String rating) {

        if (rating.contains(",")) {
            rating = rating.replace(rating.charAt(1), '.');
        }
        try {

            return Double.parseDouble(rating);
        } catch (NumberFormatException e) {
            // e.printStackTrace();
            Log.d("NumberFormatException", e.getMessage());
        }
        return -1.0;
    }

    //Method that will parse the JSON file and will return a JSONObject
    public JSONObject parseJSONData(String jSonFile) {
        String JSONString = null;
        JSONObject JSONObject = null;
        try {

            //open the inputStream to the file
            AssetManager assets = MainApplication.getAppContext().getAssets();

            InputStream inputStream = assets.open(jSonFile);

            int sizeOfJSONFile = inputStream.available();

            //array that will store all the data
            byte[] bytes = new byte[sizeOfJSONFile];

            //reading data into the array from the file
            inputStream.read(bytes);

            //close the input stream
            inputStream.close();

            JSONString = new String(bytes, "UTF-8");
            JSONObject = new JSONObject(JSONString);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        catch (JSONException x) {
            x.printStackTrace();
            return null;
        }

        return JSONObject;
    }




    public void parseMaladiesData(ArrayList<ContentProviderOperation> cpo, Uri uri) {

        JSONObject   data= null;
        JSONArray diseases = null;
        JSONArray conditions=null;
        try {

            data = this.parseJSONData("diseases.json").getJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            diseases = data.getJSONArray("diseases");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int j = 0; j < diseases.length(); j++) {
            try {
                 conditions = ((JSONObject) diseases.get(j)).getJSONArray("conditions");
               // Log.e("DATA", conditions.get(0).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//Get the JSON object from the data

try {
//THis will store "1" inside atomicNumber
    for (int i = 0; i < conditions.length(); i++) {

        //Initialise all the fields

        String title = Constants.NA;
        String id = Constants.NA;
        String released = Constants.NA;
        Double rating = -1.0;
        String rated = "normal";
        String urlPoster = Constants.NA;
        String ratingString = Constants.NA;
        String runtime = Constants.NA;
        String plot = Constants.NA;
        String genres = "";
        String thumbnailUrl = Constants.NA;

         currentCondition = conditions.getJSONObject(i);
        if (currentCondition.has(KEY_ID) && !currentCondition.isNull(KEY_ID)) {
            id = currentCondition.getString(KEY_ID);
        }
        released = currentCondition.getString(KEY_RELEASE_DATE);
        int releaseDate = 0;
        releaseDate = Integer.parseInt(released);

        if (currentCondition.has(KEY_TITLE) && !currentCondition.isNull(KEY_TITLE)) {
            title = currentCondition.getString(KEY_TITLE);


        }
        if (currentCondition.has(KEY_RUNTIME) && !currentCondition.isNull(KEY_RUNTIME) && currentCondition.length() >= 1) {
            try {
                runtime = currentCondition.getJSONArray(KEY_RUNTIME).get(0).toString();
            } catch (JSONException e) {
                Log.d("Parse error", e.getMessage());
            }
        }
        if (currentCondition.has(KEY_GENRES) && !currentCondition.isNull(KEY_GENRES)) {


            JSONArray genresArray = currentCondition.getJSONArray(KEY_GENRES);
            if (genresArray.length() == 0) return;
            if (genresArray.length() == 1)
                genres = genresArray.get(0).toString();


            else {
                String addGenres = null;


                for (int g = 1; g < genresArray.length(); g++) {
                    addGenres = ", " + genresArray.get(g).toString();

                }


                genres = genresArray.get(0) + addGenres;
            }


        }


        rated = "normal";//currentCondition.getString(KEY_RATED);
        plot = currentCondition.getString(KEY_PLOT);
        urlPoster = "http://creativebits.org/files/B_hazard.gif";
        //currentCondition.getString(KEY_URLPOSTER);
        if (currentCondition.has(KEY_RATINGS) && !currentCondition.isNull(KEY_RATINGS)) {
            ratingString = currentCondition.getString(KEY_RATINGS);
        }
        rating = getRating(ratingString);


        //INSERT THIS TO DB
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, title);
        values.put(MoviesContract.MoviesColumns.COLUMN_IMDB_ID, id);
        values.put(MoviesContract.MoviesColumns.COLUMN_PLOT, plot);
        values.put(MoviesContract.MoviesColumns.COLUMN_RATED, rated);
        values.put(MoviesContract.MoviesColumns.COLUMN_RATING, rating);
        values.put(MoviesContract.MoviesColumns.COLUMN_RELEASE_DATE, releaseDate);
        values.put(MoviesContract.MoviesColumns.COLUMN_URL_THUMBNAIL, urlPoster);
        values.put(MoviesContract.MoviesColumns.COLUMN_GENRES, genres);
        values.put(MoviesContract.MoviesColumns.COLUMN_RUNTIME, runtime);

        if (!title.equals(Constants.NA)) {
            cpo.add(ContentProviderOperation.newInsert(uri).withValues(values).build());


        }

    }
    context.getContentResolver().applyBatch(CONTENT_AUTHORITY, cpo);
}catch  (JSONException | RemoteException | OperationApplicationException e) {
            Log.d("Error updating content.", "Error updating content.", e);
}



    }


    public void parseEntitiesData(ArrayList<ContentProviderOperation> cpo, Uri uri) {

        JSONObject   data= null;
        JSONArray entities = null;
        JSONArray centers=null;
        try {

            data = this.parseJSONData("entities.json").getJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            entities = data.getJSONArray("entities");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int j = 0; j < entities.length(); j++) {
            try {
                centers = ((JSONObject) entities.get(j)).getJSONArray("healthCenters");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//Get the JSON object from the data

        try {
//THis will store "1" inside atomicNumber
            for (int i = 0; i < centers.length(); i++) {

                //Initialise all the fields

                String title = Constants.NA;
                String id = Constants.NA;
                String released = Constants.NA;
                Double rating = -1.0;
                String rated = "normal";
                String urlPoster = Constants.NA;
                String ratingString = Constants.NA;
                String runtime = Constants.NA;
                String plot = Constants.NA;
                String genres = "";
                String thumbnailUrl = Constants.NA;

                currentCondition = centers.getJSONObject(i);
                if (currentCondition.has(KEY_ID) && !currentCondition.isNull(KEY_ID)) {
                    id = currentCondition.getString(KEY_ID);
                }
                released = currentCondition.getString(KEY_RELEASE_DATE);
                int releaseDate = 0;
                releaseDate = Integer.parseInt(released);

                if (currentCondition.has(KEY_TITLE) && !currentCondition.isNull(KEY_TITLE)) {
                    title = currentCondition.getString(KEY_TITLE);


                }
                if (currentCondition.has(KEY_RUNTIME) && !currentCondition.isNull(KEY_RUNTIME) && currentCondition.length() >= 1) {
                    try {
                        runtime = currentCondition.getJSONArray(KEY_RUNTIME).get(0).toString();
                    } catch (JSONException e) {
                        Log.d("Parse error", e.getMessage());
                    }
                }
                if (currentCondition.has(KEY_GENRES) && !currentCondition.isNull(KEY_GENRES)) {


                    JSONArray genresArray = currentCondition.getJSONArray(KEY_GENRES);
                    if (genresArray.length() == 0) return;
                    if (genresArray.length() == 1)
                        genres = genresArray.get(0).toString();


                    else {
                        String addGenres = null;


                        for (int g = 1; g < genresArray.length(); g++) {
                            addGenres = ", " + genresArray.get(g).toString();

                        }


                        genres = genresArray.get(0) + addGenres;
                    }


                }


                rated = "normal";//currentCondition.getString(KEY_RATED);
                plot = currentCondition.getString(KEY_PLOT);
                urlPoster ="https://image.spreadshirtmedia.com/image-server/v1/compositions/104775043/views/1,width=300,height=300,appearanceId=1,version=1440417743/medical-cross-symbol-t-shirts-men-s-t-shirt.jpg";
                        currentCondition.getString(KEY_URLPOSTER);
                if (currentCondition.has(KEY_RATINGS) && !currentCondition.isNull(KEY_RATINGS)) {
                    ratingString = currentCondition.getString(KEY_RATINGS);
                }
                rating = getRating(ratingString);


                //INSERT THIS TO DB
                ContentValues values = new ContentValues();

                values.put(COLUMN_TITLE, title);
                values.put(MoviesContract.MoviesColumns.COLUMN_IMDB_ID, id);
                values.put(MoviesContract.MoviesColumns.COLUMN_PLOT, plot);
                values.put(MoviesContract.MoviesColumns.COLUMN_RATED, rated);
                values.put(MoviesContract.MoviesColumns.COLUMN_RATING, rating);
                values.put(MoviesContract.MoviesColumns.COLUMN_RELEASE_DATE, releaseDate);
                values.put(MoviesContract.MoviesColumns.COLUMN_URL_THUMBNAIL, urlPoster);
                values.put(MoviesContract.MoviesColumns.COLUMN_GENRES, genres);
                values.put(MoviesContract.MoviesColumns.COLUMN_RUNTIME, runtime);

                if (!title.equals(Constants.NA)) {
                    cpo.add(ContentProviderOperation.newInsert(uri).withValues(values).build());


                }

            }
            context.getContentResolver().applyBatch(CONTENT_AUTHORITY, cpo);
        }catch  (JSONException | RemoteException | OperationApplicationException e) {
            Log.d("Error updating content.", "Error updating content.", e);
        }



    }

}
