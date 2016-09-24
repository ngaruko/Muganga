package org.muganga;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.muganga.Logs.Logger;
import org.muganga.activities.GlobalSearchActivity;
import org.muganga.fragments.AskDoctorFragment;
import org.muganga.fragments.ChatFragment;
import org.muganga.fragments.ChatMessageFragment;
import org.muganga.fragments.ConnectFragment;
import org.muganga.fragments.LocationsFragment;
import org.muganga.fragments.dummy.DummyContent;
import org.muganga.services.MoviesService;
import org.muganga.tabs.SlidingTabLayout;
import org.muganga.utilities.LogHelper;
import org.muganga.utilities.SortListener;


public class MainActivity extends AppCompatActivity implements ChatMessageFragment.OnListFragmentInteractionListener, ChatFragment.OnFragmentInteractionListener {


    public static final int TAB_HOME = 0;
    public static final int TAB_ASK_DOCTOR = 2;
    public static final int TAB_LOCATIONS = 1;

    public static final int TAB_CONTACT = 3;


    public static final int TAB_COUNT = 4;


    private Toolbar toolbar;

    private ViewPager mPager;
    private SlidingTabLayout mTabs;


    private MyPagerAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
//Todo: Too many hard coded strings that need to be initiated elsewhere, either string or constants

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transition_a));
        }
        super.onCreate(savedInstanceState);
// Call the service to get all movies
      startService(new Intent(this, MoviesService.class));



        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);


        //Initialise mpager and mTabs

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setOffscreenPageLimit(5);
        //For tabs
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());


        mPager.setAdapter(mAdapter);
        // Attach the page change listener inside the activity


        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);


        mTabs.setViewPager(mPager);



    }

    @Override
    protected void onStart() {
        super.onStart();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            Log.w("Internet", "Not online, not refreshing.");


            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Connection Error");
            alertDialog.setMessage("Please check your internet connection!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.getContext();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            try {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        handleSearchView(searchView);


        return true;
    }

    private void handleSearchView(final SearchView search) {
        search.setQueryHint("Hospitals, Doctors, Health Centers,Pharmacies, Etc");
        search.setSubmitButtonEnabled(true);
        //*** setOnQueryTextFocusChangeListener ***
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO To add more code in Phase 2

            }
        });

        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
//todo here
                //mTitle=query;
                search.clearFocus();
                //display the Progress bar
//                progressbarView.setVisibility(View.VISIBLE);

                searchForMovies(query);

                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO To do more tweaks in phase two or later

                return false;
            }


        });
    }


    public void searchForMovies(String searchQuery) {

        //todo a asunc tast to get movies




        if (searchQuery != null) {


            LogHelper.log("Sending intent from Main: " + searchQuery);
            Intent intent = new Intent(this, GlobalSearchActivity.class);

            intent.putExtra("title_extra", searchQuery);
            startActivity(intent);
        } else
            Toast.makeText(this, "Please enter a valid, non-empty search query!! ", Toast.LENGTH_LONG).show();


        //todo call fragment


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh) this.startService(new Intent(this, MoviesService.class));

        //call instantiate item since getItem may return null depending on whether the PagerAdapter is of type FragmentPagerAdapter or FragmentStatePagerAdapter
        Fragment fragment = (Fragment) mAdapter.instantiateItem(mPager, mPager.getCurrentItem());

        if (fragment instanceof SortListener) {


            switch (id) {


                case R.id.refresh:
                    try {
                        ((SortListener) fragment).onRefresh();
                        Log.d("rfress", "refereshin");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;


                case R.id.action_sort_title:
                    try {
                        Log.d("title", "tile");
                        ((SortListener) fragment).onSortTitle();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;


                case R.id.action_sort_date:
                    try {
                        Log.d("date", "date");
                        ((SortListener) fragment).onSortByDate();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;


                case R.id.action_sort_rating:
                    try {
                        Log.d("rating", "rating");
                        ((SortListener) fragment).onSortByRating();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Logger.longToast("Some change");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Logger.longToast("Some change");
    }


    //Construct an adapter

    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        //Initialise string-array for tabs
        String[] tabs = getResources().getStringArray(R.array.tabs);


        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

        }

        @Override
        public Fragment getItem(int position) {


            Fragment fragment = null;


            switch (position) {

                case TAB_HOME:
                    fragment = AskDoctorFragment.newInstance("", "");

                    break;

                case TAB_LOCATIONS:
                    fragment = LocationsFragment.newInstance("", "");


                    break;

                case TAB_ASK_DOCTOR:
                    //fragment = ChatMessageFragment.newInstance(1);
                    fragment = ChatFragment.newInstance("","");
                    break;


                case TAB_CONTACT:
                    fragment = ConnectFragment.newInstance("", "");

                    break;

            }
            return fragment;
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {

            return TAB_COUNT;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }


}
