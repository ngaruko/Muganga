package org.muganga.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MoviesHelper extends SQLiteOpenHelper {


    private static final String TABLE_CREATOR = " (" +
            MoviesContract.MoviesColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MoviesContract.MoviesColumns.COLUMN_TITLE + " TEXT," +
            MoviesContract.MoviesColumns.COLUMN_RELEASE_DATE + " INTEGER," +
            MoviesContract.MoviesColumns.COLUMN_RATING + " DOUBLE," +
            MoviesContract.MoviesColumns.COLUMN_PLOT + " TEXT," +
            MoviesContract.MoviesColumns.COLUMN_URL_THUMBNAIL + " TEXT," +
            MoviesContract.MoviesColumns.COLUMN_RATED + " TEXT," +
            MoviesContract.MoviesColumns.COLUMN_GENRES + " TEXT," +
            MoviesContract.MoviesColumns.COLUMN_RUNTIME + " INTEGER," +
            MoviesContract.MoviesColumns.COLUMN_SIMPLE_PLOT + " TEXT," +
            MoviesContract.MoviesColumns.COLUMN_TRAILER_URL + " TEXT," +
            MoviesContract.MoviesColumns.COLUMN_URLIMDB + " TEXT," +
            MoviesContract.MoviesColumns.COLUMN_IMDB_ID + " TEXT" +
            ");";


    private static final String DB_NAME = "movies_db";
    private static final int DB_VERSION = 15;

    public MoviesHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Context mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL("CREATE TABLE " + MoviesProvider.Tables.IN_THEATERS + TABLE_CREATOR);
            db.execSQL("CREATE TABLE " + MoviesProvider.Tables.COMING_SOON + TABLE_CREATOR);
            db.execSQL("CREATE TABLE " + MoviesProvider.Tables.TOP_MOVIES + TABLE_CREATOR);
            db.execSQL("CREATE TABLE " + MoviesProvider.Tables.BOTTOM_MOVIES + TABLE_CREATOR);
            db.execSQL("CREATE TABLE " + MoviesProvider.Tables.FOUND_MOVIES + TABLE_CREATOR);

        } catch (SQLiteException exception) {
            Log.e("OnCreate Error", exception + "");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Log.e("On Upgrade", "upgrade table executed");

            db.execSQL("DROP TABLE IF EXISTS " + MoviesProvider.Tables.IN_THEATERS);
            db.execSQL("DROP TABLE IF EXISTS " + MoviesProvider.Tables.COMING_SOON);
            db.execSQL("DROP TABLE IF EXISTS " + MoviesProvider.Tables.TOP_MOVIES);
            db.execSQL("DROP TABLE IF EXISTS " + MoviesProvider.Tables.FOUND_MOVIES);
            db.execSQL("DROP TABLE IF EXISTS " + MoviesProvider.Tables.BOTTOM_MOVIES);
            onCreate(db);
        } catch (SQLiteException exception) {
            Log.e("SQL Update Error", exception + "");
        }
    }
}
