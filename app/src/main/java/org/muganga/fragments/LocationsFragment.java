package org.muganga.fragments;


import android.app.LoaderManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.muganga.Logs.Logger;
import org.muganga.MainApplication;
import org.muganga.R;
import org.muganga.adapters.AdapterEntities;
import org.muganga.data.MovieLoader;
import org.muganga.utilities.MovieSorter;
import org.muganga.utilities.SortListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,SortListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG_SORT_TITLE = "sortTitle";
    //tag associated with the  menu button that sorts by date
    private static final String TAG_SORT_DATE = "sortDate";
    //tag associated with the  menu button that sorts by ratings

    private static final String TAG_SORT_RATING = "sortRating";

    private RecyclerView mRecyclerView;
    private AdapterEntities mAdapter;
    private Context mContext;

    public LocationsFragment() {
        // Required empty public constructor
    }

    public static android.support.v4.app.Fragment newInstance(String param1, String param2) {
        LocationsFragment fragment = new LocationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
mContext= MainApplication.getAppContext();

        getActivity().getLoaderManager().initLoader(1, null, this);

        if (savedInstanceState == null) {

            //  getActivity().startService(new Intent(getActivity(), MoviesService.class));

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MovieSorter.Filter.setFilterString("");
        View view= inflater.inflate(R.layout.fragment_top_movies, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.top_movies_recyclerView);

        return view;
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return MovieLoader.newAllTopMoviesInstance(mContext);
        //  return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {

        if (isAdded()) {
            int columnCount = getResources().getInteger(R.integer.list_column_count);

            StaggeredGridLayoutManager sglm2 =
                    new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
            
            mRecyclerView.setLayoutManager(sglm2);


             mAdapter = new AdapterEntities(cursor, getActivity());
            mAdapter.setHasStableIds(true);
            try {
                mRecyclerView.setAdapter(mAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {

            Logger.error("Not added the thing");

        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        mRecyclerView.setAdapter(null);
    }

    @Override
    public void onSortTitle() {
        MovieSorter.Sort.setSortString(TAG_SORT_TITLE);

        getActivity().getLoaderManager().restartLoader(1, null, this);
        Snackbar
                .make(getView(), "Sorted by Title", Snackbar.LENGTH_LONG)
                .setAction("OK", null)
                .show();

    }

    @Override
    public void onSortByDate() {
        MovieSorter.Sort.setSortString(TAG_SORT_DATE);

        getActivity().getLoaderManager().restartLoader(1, null, this);
        Snackbar
                .make(getView(), "Sorted by Date", Snackbar.LENGTH_LONG)
                .setAction("OK", null)
                .show();

    }

    @Override
    public void onSortByRating() {

        MovieSorter.Sort.setSortString(TAG_SORT_RATING);
        getActivity().getLoaderManager().restartLoader(1,null, this);


        Snackbar
                .make(getView(), "Sorted by Rating", Snackbar.LENGTH_LONG)
                .setAction("OK", null)
                .show();

    }

    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public void onFilter(String filterText) {

        MovieSorter.Filter.setFilterString(filterText);
        getActivity().getLoaderManager().restartLoader(1,null,this);
        //MovieSorter.Filter.setFilterString("");


    }

    private void refresh() {
        //getActivity().startService(new Intent(getActivity(), MoviesService.class));
        getActivity().getLoaderManager().restartLoader(1, null, this);
    }
}