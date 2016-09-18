package org.muganga.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import org.muganga.R;
import org.muganga.utilities.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FetchAddressIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "org.muganga.services.action.FOO";
    private static final String ACTION_BAZ = "org.muganga.services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "org.muganga.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "org.muganga.services.extra.PARAM2";
    private static final String TAG ="Error" ;
    protected ResultReceiver mReceiver;
    private List<Address> mAddresses;
    private android.location.Geocoder mGeocoder;
    private String mErrorMessage="";
    private String mQuery;

    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

         mGeocoder=new Geocoder(this, Locale.getDefault());

//mGeocoder.getFromLocationName("auckland",5,)

       mAddresses = null;
        //lets try some known name of a place

        if (intent != null) {
            mReceiver = intent.getParcelableExtra(Constants.RECEIVER);
            final String action = intent.getAction();

            getUserLocation(intent);

           switch (action){
               case Constants.FIND_USER_LOCATION:
                   getUserLocation(intent);
                   break;
               case Constants.FIND_NEAREST_HOSPITAL:
                 //  mQuery=intent.getParcelableExtra("query");
                   getNearestHospital(intent);

           }

        }
    }

    private void getNearestHospital(Intent intent) {

        String locality=intent.getStringExtra(Constants.QUERY);

        try {

            mAddresses=mGeocoder.getFromLocationName("regina mundi, burundi",3);
            Log.e("Found places: ", String.valueOf(mAddresses.size()) + "===" + String.valueOf(mAddresses) );
            for (Address address:mAddresses) {
               Log.e("Found address name: ", address.getCountryName());

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
Address address=mAddresses.get(0);
        Log.e("Address: ", address.getCountryName());
        deliverResultToReceiver(Constants.SUCCESS_RESULT,address.getAdminArea());
    }

    private void getUserLocation(Intent intent) {
        // Get the location passed to this service through an extra.
        Location location =intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);


        try {

            mAddresses = mGeocoder.getFromLocation(location.getLatitude(),location.getLongitude(),
                    // In this sample, get just a single address.
                    1);


        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            mErrorMessage = getString(R.string.service_not_available);
            Log.d(TAG, mErrorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            mErrorMessage = getString(R.string.invalid_lat_long_used);
            Log.d(TAG, mErrorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (mAddresses == null || mAddresses.size()  == 0) {
            if (mErrorMessage.isEmpty()) {
                mErrorMessage = getString(R.string.no_address_found);
                Log.d(TAG, mErrorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, mErrorMessage);
        } else {
            Address address = mAddresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, getString(R.string.address_found));
           deliverResultToReceiver(Constants.SUCCESS_RESULT,
                   TextUtils.join(System.getProperty("line.separator"),
                           addressFragments));

        }
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        Log.d("Delivering " + resultCode, message);
       mReceiver.send(resultCode, bundle);

    }


}
