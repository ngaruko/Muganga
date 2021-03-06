package org.muganga.fragments;


import android.app.LoaderManager;
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
import org.muganga.R;
import org.muganga.adapters.AdapterDiseases;
import org.muganga.data.MovieLoader;
import org.muganga.utilities.MovieSorter;
import org.muganga.utilities.SortListener;


public class AskDoctorFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SortListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private static final String TAG_SORT_TITLE = "sortTitle";
    //tag associated with the  menu button that sorts by date
    private static final String TAG_SORT_DATE = "sortDate";
    //tag associated with the  menu button that sorts by ratings

    private static final String TAG_SORT_RATING = "sortRating";
    private static final String CONNECTIVITY_SERVICE = "connectivity";

    private RecyclerView mRecyclerView;
    private View mView;
    private View mProgressbar;
    private AdapterDiseases mAdapter;

    public AskDoctorFragment() {
        // Required empty public constructor
    }

    public static AskDoctorFragment newInstance(String param1, String param2) {
        AskDoctorFragment fragment = new AskDoctorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getActivity().getLoaderManager().initLoader(0, null, this);
Logger.error(this.getArguments().getString(ARG_PARAM1));

        if (savedInstanceState == null) {
            refresh();

        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//clean the text view
        MovieSorter.Filter.setFilterString("");
        mView = inflater.inflate(R.layout.fragment_in_theaters, container, false);


        mRecyclerView = (RecyclerView) mView.findViewById(R.id.in_theaters_recyclerView);

        return mView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {


        return MovieLoader.newAllInTheatersMoviesInstance(getActivity());


    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {


        if (isAdded()) {
            int columnCount = getResources().getInteger(R.integer.list_column_count);
            StaggeredGridLayoutManager sglm =
                    new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(sglm);

            mAdapter = new AdapterDiseases(cursor, getActivity());
            mAdapter.setHasStableIds(true);
            try {
                mRecyclerView.setAdapter(mAdapter);

            } catch (Exception e) {
                e.printStackTrace();

            }
            
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        mRecyclerView.setAdapter(null);
    }


    @Override
    public void onSortTitle() {
        MovieSorter.Sort.setSortString(TAG_SORT_TITLE);

        getActivity().getLoaderManager().restartLoader(0, null, this);
        Snackbar
                .make(getView(), "Sorted by Title", Snackbar.LENGTH_LONG)
                .setAction("OK", null)
                .show();

    }

    @Override
    public void onSortByDate() {
        MovieSorter.Sort.setSortString(TAG_SORT_DATE);

        getActivity().getLoaderManager().restartLoader(0, null, this);
        Snackbar
                .make(getView(), "Sorted by Date", Snackbar.LENGTH_LONG)
                .setAction("OK", null)
                .show();

    }

    @Override
    public void onSortByRating() {

        MovieSorter.Sort.setSortString(TAG_SORT_RATING);
        getActivity().getLoaderManager().restartLoader(0, null, this);


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
        //todo do some filtering...
        Logger.longToast("Filtering-----");
        //final List<Movie> filteredModelList = filter(mMovie, filterText);
      //  m.setFilter(filteredModelList);

        MovieSorter.Filter.setFilterString(filterText);
        getActivity().getLoaderManager().restartLoader(0,null,this);
        MovieSorter.Filter.setFilterString("");


    }

    private void refresh() {
        //getActivity().startService(new Intent(getActivity(), MoviesService.class));
        getActivity().getLoaderManager().restartLoader(0, null, this);
    }
}



