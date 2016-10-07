package org.muganga;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.muganga.Logs.Logger;
import org.muganga.fragments.AskDoctorFragment;
import org.muganga.fragments.ChatFragment;
import org.muganga.fragments.ChatMessageFragment;
import org.muganga.fragments.HomeFragment;
import org.muganga.fragments.LocationsFragment;
import org.muganga.fragments.dummy.DummyContent;
import org.muganga.services.MoviesService;
import org.muganga.tabs.SlidingTabLayout;
import org.muganga.utilities.SortListener;

import java.util.List;


public class MainActivity extends AppCompatActivity implements
        ChatMessageFragment.OnListFragmentInteractionListener,
        ChatFragment.OnFragmentInteractionListener,
        SearchView.OnQueryTextListener,
        GoogleApiClient.OnConnectionFailedListener,
        FragmentManager.OnBackStackChangedListener{


    public static final int TAB_HOME = 0;
    public static final int TAB_INFO = 1;
    public static final int TAB_LOCATIONS = 2;
    public static final int TAB_ASK_DOCTOR = 3;
    public static final int TAB_COUNT = 4;


    private Toolbar toolbar;

    private ViewPager mPager;
    private SlidingTabLayout mTabs;

    private MyPagerAdapter mAdapter;
    public Fragment mFragment;

    public MenuItem mSearchItem;
    private GoogleApiClient mGoogleApiClient;
    private SearchView mSearchView;


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
        mPager.setOffscreenPageLimit(4);
        //For mTabsArray
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
       //mAdapter = new MyPagerAdapter(this.));
       // setAdapter(new PagerAdapter(getActivity().getChildFragmentmanager()));

        mPager.setAdapter(mAdapter);
        // Attach the page change listener inside the activity


        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);


        mTabs.setViewPager(mPager);

        //call instantiate item since getItem may return null depending on whether the PagerAdapter is of type FragmentPagerAdapter or FragmentStatePagerAdapter

        mFragment = (Fragment) mAdapter.instantiateItem(mPager, mPager.getCurrentItem());


             // Logger.error("Initial Fragment: "  + mFragment.toString());

        //Google stuff
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,0 /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


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
        Fragment currentFragment = null;
        mSearchItem = menu.findItem(R.id.action_search);
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){

                if(fragment != null && fragment.isVisible())
                    currentFragment= fragment;
            }
        }

       if (currentFragment instanceof SortListener) {

            mSearchItem = menu.findItem(R.id.action_search);
           mSearchItem.setVisible(true);
            mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);

            mSearchItem.setVisible(true);
        mSearchView.setQueryHint("Search...");
        mSearchView.setSubmitButtonEnabled(true);
            //*** setOnQueryTextFocusChangeListener ***

            //*** setOnQueryTextListener ***
        mSearchView.setOnQueryTextListener(this);

       }else{
           mSearchItem.setVisible(false);
        }





        return true;
    }


    @Override
    public boolean onQueryTextChange(String filterText) {
        Fragment fragment=(Fragment) mAdapter.instantiateItem(mPager, mPager.getCurrentItem());

        if (fragment instanceof SortListener) {
            Logger.error("Not an instance of SortListener");
            ((SortListener) fragment).onFilter(filterText);
        } else {
            Logger.error("Not an instance of SortListener" + fragment.toString());
            return  false;
        }

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
       // Logger.longToast(mFragment.toString());
        // TODO: 25-Sep-16  some search fuinctionality
        return false;
    }



















    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh) this.startService(new Intent(this, MoviesService.class));



        if (mFragment instanceof SortListener) {


            switch (id) {


                case R.id.refresh:
                    try {
                        ((SortListener) mFragment).onRefresh();
                        Logger.longToast(mFragment.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;


                case R.id.action_sort_title:
                    try {
                        Logger.longToast(mFragment.toString());
                        ((SortListener) mFragment).onSortTitle();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;


                case R.id.action_sort_date:
                    try {
                        Log.d("date", "date");
                        ((SortListener) mFragment).onSortByDate();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;


                case R.id.action_sort_rating:
                    try {
                        Log.d("rating", "rating");
                        ((SortListener) mFragment).onSortByRating();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.action_search:
                    try {
                      Logger.longToast(mFragment.toString());
                        //((SortListener) mFragment).onSortByRating();

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
        //todo not sure what to do here
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // TODO: 26-Sep-16 handle failed connection
    }

    @Override
    public void onBackStackChanged() {
        mSearchView.setQuery("", true);
      mSearchView.setIconified(false);
       // mSearchView.clearFocus();
        Logger.error("Fragment changed");
    }

    public void hideSearch() {
        mSearchItem.setVisible(false);
    }


    //Construct an adapter

    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        //Initialise string-array for mTabsArray
        String[] mTabsArray = getResources().getStringArray(R.array.tabs);


        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

        }

        @Override
        public Fragment getItem(int position) {





            switch (position) {

                case TAB_HOME:

                    mFragment = HomeFragment.newInstance("", "");



                    break;

                case TAB_INFO:



                    mFragment = AskDoctorFragment.newInstance("", "");
                    break;

                case TAB_LOCATIONS:

                    mFragment = LocationsFragment.newInstance("", "");
                    break;


                case TAB_ASK_DOCTOR:
                    mFragment = ChatFragment.newInstance("","");

                    break;

            }



            Logger.error("Fragment changed");

            return mFragment;

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
            return mTabsArray[position];
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Logger.error("Fragment resumed");
    }


}
