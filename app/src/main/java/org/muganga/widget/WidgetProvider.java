package org.muganga.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.muganga.services.MoviesService;
import org.muganga.utilities.LogHelper;


public class WidgetProvider extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {


        context.startService(new Intent(context, WidgetIntentService.class));
        Log.e("Update", "update");
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, WidgetIntentService.class));

        Log.e("OptionsChanged", "onAppWidgetOptionsChanged");
    }



    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogHelper.log("onReceive action=" + action);
        Log.e("OnReceive action", action);
        updateDatabase(context);
        super.onReceive(context, intent);
        context.startService(new Intent(context, WidgetIntentService.class));


    }

    public void updateDatabase(Context context) {


        Intent service_start = new Intent(context, MoviesService.class);
        context.startService(service_start);

        Log.e("Updating Db", "db");
    }


}