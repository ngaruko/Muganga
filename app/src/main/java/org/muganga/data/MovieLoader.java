package org.muganga.data;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;



public class MovieLoader extends CursorLoader {
    private MovieLoader(Context context, Uri uri) {
        //super(context, uri, Query.PROJECTION, null, null, MoviesContract.InTheater.getSortString());
        super(context, uri, Query.PROJECTION, MoviesContract.InTheater.getFilterString(), null, MoviesContract.InTheater.getSortString());

    }



    public static MovieLoader newAllInTheatersMoviesInstance(Context context) {
        return new MovieLoader(context, MoviesContract.InTheater.buildDirUri());
    }

    public static MovieLoader newAllTopMoviesInstance(Context context) {
        return new MovieLoader(context, MoviesContract.TopMovies.buildDirUri());
    }

    public static MovieLoader newAllComingSoonMoviesInstance(Context context) {
        return new MovieLoader(context, MoviesContract.ComingSoon.buildDirUri());
    }

    public static MovieLoader newInstanceForItemId(Context context, long itemId) {
        return new MovieLoader(context, MoviesContract.InTheater.buildItemUri(itemId));
    }

    public static MovieLoader newBootomMovieForItemId(Context context, long itemId) {
        return new MovieLoader(context, MoviesContract.BottomMovies.buildItemUri(itemId));
    }

    public static MovieLoader newComingSoonForItemId(Context context, long itemId) {
        return new MovieLoader(context, MoviesContract.ComingSoon.buildItemUri(itemId));
    }


    public static MovieLoader newFoundForItemId(Context context, long itemId) {
        return new MovieLoader(context, MoviesContract.FoundMovies.buildItemUri(itemId));
    }

    public static MovieLoader newTopMovieForItemId(Context context, long itemId) {
        return new MovieLoader(context, MoviesContract.TopMovies.buildItemUri(itemId));
    }
    public static MovieLoader newAllBottomMoviesInstance(Context context) {
        return new MovieLoader(context, MoviesContract.BottomMovies.buildDirUri());
    }

    public static MovieLoader newAllFoundMoviesInstance(Context context) {
        return new MovieLoader(context, MoviesContract.FoundMovies.buildDirUri());
    }
    public static MovieLoader newAllFilteredMoviesInstance(Context context) {
        return new MovieLoader(context, MoviesContract.FoundMovies.buildDirUri());
    }



    public interface Query {
        String[] PROJECTION = {

                MoviesContract.InTheater._ID,
                MoviesContract.InTheater.COLUMN_TITLE,
                MoviesContract.InTheater.COLUMN_RELEASE_DATE,
                MoviesContract.InTheater.COLUMN_RATING,
                MoviesContract.InTheater.COLUMN_PLOT,
                MoviesContract.InTheater.COLUMN_URL_THUMBNAIL,
                MoviesContract.InTheater.COLUMN_RATED,
                MoviesContract.InTheater.COLUMN_GENRES,
                MoviesContract.InTheater.COLUMN_IMDB_ID,
                MoviesContract.InTheater.COLUMN_RUNTIME
        };


     int _ID=0;
      int COLUMN_TITLE=1;
      int COLUMN_RELEASE_DATE=2;
      int COLUMN_RATING=3;
      int COLUMN_PLOT=4;
      int COLUMN_URL_THUMBNAIL=5;
      int COLUMN_RATED=6;
        int COLUMN_GENRES=7;
       int COLUMN_IMDB_ID=8;
        int COLUMN_RUNTIME=9;
       

    }
}
