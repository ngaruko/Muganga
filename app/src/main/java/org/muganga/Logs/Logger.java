package org.muganga.Logs;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.muganga.MainApplication;

/**
 * Created by itl on 2/07/2015.
 */
public class Logger {
    public static void debug(String message) {
        Log.d("Logged", "" + message);
    }
    public static void error (String message) {
        Log.e("Logger", "" + message);
    }

    public static void shortTost(Context context, String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
    }
    public static void longToast(String message) {
        Toast.makeText(MainApplication.getAppContext(), message + "", Toast.LENGTH_LONG).show();
    }

}