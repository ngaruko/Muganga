package org.muganga.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import org.muganga.MainApplication;
import org.muganga.models.UserEvent;


/**
 * Created by bede.ngaruko on 17-Sep-16.
 */
public class LocationHelper {
    private static final int ONE_KILOMETER = 1000;
    public static String bestProvider;
    private static Context context;
    //  private static context mContext=MainApplication.getAppContext();
    private static Criteria mCriteria ;

    public static boolean eventIsInRange(Activity activity, UserEvent event) {
        return eventIsInRange(event, getLastKnownLocation(activity));
    }

    public static boolean eventIsInRange(UserEvent event, Location lastKnownLocation) {
        Location eventLocation = new Location("eventLocation");
        eventLocation.setLatitude(event.getLatitude());
        eventLocation.setLongitude(event.getLongitude());
        if (lastKnownLocation != null && lastKnownLocation.distanceTo(eventLocation) < ONE_KILOMETER) {
            return true;
        }
        return false;
    }

    public static LocationManager getLocationManager(Activity activity) {
        return (LocationManager) activity
                .getSystemService(Context.LOCATION_SERVICE);

    }

    public static Location getLastKnownLocation(Activity activity) {
        mCriteria = new Criteria();
        context = MainApplication.getAppContext();
        mCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
        bestProvider = getLocationManager(activity).getBestProvider(mCriteria, true);


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return TODO;
        }
        return getLocationManager(activity).getLastKnownLocation(bestProvider);
    }

    public static void updateLocation(Activity activity, LocationListener locationListener) {


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getLocationManager(activity).requestLocationUpdates(bestProvider, 400, 1, locationListener);

    }

    public static String getBestProvider(Activity activity) {
        return getLocationManager(activity).getBestProvider(mCriteria, true);
    }
}
