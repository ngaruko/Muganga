package org.muganga.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.muganga.Callbacks.ItemLoadedListener;
import org.muganga.R;
import org.muganga.data.Movie;
import org.muganga.utilities.LogHelper;

public class GlobalSearchActivity extends AppCompatActivity implements ItemLoadedListener {


    public Movie mMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }


        setContentView(R.layout.activity_movie_search);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_movie_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieLoded(Movie movie) {
//mProgressbar.setVisibility(View.GONE);
        mMovie = new Movie();


        LogHelper.log("Movie laoded!");
    }
}
