package org.muganga.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.os.ResultReceiver;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.muganga.Logs.Logger;
import org.muganga.MainApplication;
import org.muganga.R;
import org.muganga.services.FetchAddressIntentService;
import org.muganga.utilities.Constants;
import org.muganga.utilities.LocationHelper;

public class FindActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    public String mAddressOutput;
    private  String mLocationSearch="auckland";
    private String mQuery="auckland";

    //method to start intent
    protected void findUserLocation() {

        mResultReceiver = new AddressResultReceiver(null);
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.setAction(Constants.FIND_USER_LOCATION);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        mLastLocation= LocationHelper.getLastKnownLocation(this);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }


    protected void findNearestHospital(String query) {
mQuery=query;
        mResultReceiver = new AddressResultReceiver(null);
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.setAction(Constants.FIND_NEAREST_HOSPITAL);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        mLastLocation= LocationHelper.getLastKnownLocation(this);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        intent.putExtra(Constants.QUERY, mQuery);
        startService(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, mAddressOutput, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //call start intent

        findUserLocation();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Gets the best and most recent location currently available,
        // which may be null in rare cases when a location is not available.
       // mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mLastLocation=LocationHelper.getLastKnownLocation(this);

        if (mLastLocation != null) {
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                Toast.makeText(this, R.string.no_geocoder_available,
                        Toast.LENGTH_LONG).show();
                return;
            }

           // if (mAddressRequested) {
                //findUserLocation();


           // }
        }
    }
       // You must also start the intent service when the connection to Google Play services is established, if the user has already clicked the button on your app's UI.
        //The following code snippet shows the call to the findUserLocation() method in the onConnected() callback provided by the Google API Client:


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_RESULTS_FROM_LOCATION_SERVICE ="location_results" ;

        FindActivity activity;
        private String location="burundi";


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_RESULTS_FROM_LOCATION_SERVICE, "YOU GOT HERE");
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_find, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
           textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            final TextView results = (TextView) rootView.findViewById(R.id.section_results);
            //results.setText(getArguments().getString(ARG_RESULTS_FROM_LOCATION_SERVICE));

            Button findUser=(Button) rootView.findViewById(R.id.find_user_location);
            Button findHospital=(Button) rootView.findViewById(R.id.nearest_hospital);
            if(getActivity() instanceof FindActivity){
                 activity = (FindActivity) getActivity();

            }


            findUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   activity.findUserLocation();

                    results.setText(activity.grabResults());
                }
            });

            findHospital.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.findNearestHospital(location);
                    results.setText(activity.grabResults());
                }
            });
            return rootView;
        }
    }

    private String grabResults() {
        return mAddressOutput;

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }


    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
           mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
           displayAddressOutput(mAddressOutput);
            Log.d("Address output: ", mAddressOutput);

            
            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                Logger.longToast(getString(R.string.address_found));
            }

        }
    }

    private void displayAddressOutput(String mAddressOutput) {
        Toast.makeText(MainApplication.getAppContext(),mAddressOutput,Toast.LENGTH_LONG).show();
    }
}
