package org.muganga.adapters;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import org.muganga.R;
import org.muganga.VolleySingleton;
import org.muganga.activities.ItemDetailActivity;
import org.muganga.data.MovieLoader;
import org.muganga.data.MoviesContract;


public class AdapterDiseases extends RecyclerView.Adapter<AdapterDiseases.ViewHolderMovies> {
    private Cursor mCursor;
    private LayoutInflater mLayoutInflater;

    private VolleySingleton mVolleySingleton;
    private ImageLoader mImageLoader;
    private Activity context;


    public AdapterDiseases(Cursor cursor, Activity context) {
        this.context = context;


        mCursor = cursor;
        try {
            mLayoutInflater = LayoutInflater.from(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mVolleySingleton = VolleySingleton.getInstance();
        mImageLoader = mVolleySingleton.getImageLoader();


    }


    @Override
    public long getItemId(int position) {
        try {
            mCursor.moveToPosition(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCursor.getLong(MovieLoader.Query._ID);
    }

    @Override
    public ViewHolderMovies onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mLayoutInflater.inflate(R.layout.movie_item_layout, parent, false);

        final ViewHolderMovies vh = new ViewHolderMovies(view);

        //final ActionBarActivity selfContext =this;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ItemDetailActivity.class);

                try {


                    if (Build.VERSION.SDK_INT >= 21) {

                        View viewStart = view.findViewById(R.id.movieThumbnail);


                        viewStart.setTransitionName("transition");
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, viewStart, viewStart.getTransitionName());


                        Uri uri = MoviesContract.InTheater.buildItemUri(getItemId(vh.getAdapterPosition()));
                        intent.setData(uri);

                        intent.putExtra("fragment", "theaters");


                        ActivityCompat.startActivity(context, intent, options.toBundle());
                    } else {

                        Intent i = new Intent(context, ItemDetailActivity.class);
                        Uri uri = MoviesContract.InTheater.buildItemUri(getItemId(vh.getAdapterPosition()));
                        i.setData(uri);

                        i.putExtra("fragment", "theaters");
                        context.startActivity(i);
                    }


                } catch (Exception e) {
                        Log.d("Intent Error", e.getMessage());
                    }


                Log.d("position", String.valueOf(getItemId(vh.getAdapterPosition())));
            }
            }

        );
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolderMovies holder, int position) {
        mCursor.moveToPosition(position);
        holder.movieTitle.setText(mCursor.getString(MovieLoader.Query.COLUMN_TITLE));


        // holder.type.setText(mCursor.getString(MovieLoader.Query.COLUMN_RELEASE_DATE));
        holder.movieReleaseDate.setText(mCursor.getString(MovieLoader.Query.COLUMN_GENRES));
        //Double rating = mCursor.getDouble(MovieLoader.Query.COLUMN_RATING);


       // holder.movieThumbnail.setImageResource(R.drawable.google_plus);
        holder.movieThumbnail.setImageResource(R.drawable.disease);

    }



    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }


    public class ViewHolderMovies extends RecyclerView.ViewHolder {

        ImageView movieThumbnail;
        TextView movieTitle;
        TextView movieReleaseDate;
        //RatingBar movieRating;


        public ViewHolderMovies(View itemView) {
            super(itemView);

            movieThumbnail = (ImageView) itemView.findViewById(R.id.movieThumbnail);
            movieTitle = (TextView) itemView.findViewById(R.id.movie_title);
            movieReleaseDate = (TextView) itemView.findViewById(R.id.movieReleaseDate);
            //movieRating = (RatingBar) itemView.findViewById(R.id.movieRating);
        }
    }
}