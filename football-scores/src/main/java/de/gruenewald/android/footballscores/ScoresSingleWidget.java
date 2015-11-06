/*
 * *
 *  * ****************************************************************************
 *  * Copyright (c) 2015 by Jan Grünewald.
 *  * jan.gruenewald84@googlemail.com
 *  * <p>
 *  * This file is part of 'Super Duo'. 'Super Duo' was developed as
 *  * part of the Android Developer Nanodegree by Udacity.
 *  * <p>
 *  * 'Super Duo' is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  * <p>
 *  * 'Super Duo' is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  * <p>
 *  * You should have received a copy of the GNU General Public License
 *  * along with 'Super Duo'.  If not, see <http://www.gnu.org/licenses/>.
 *  * ****************************************************************************
 *
 */

package de.gruenewald.android.footballscores;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.service.MyWidgetUpdateService;

/**
 * Implementation of App Widget functionality.
 */
public class ScoresSingleWidget extends AppWidgetProvider {
    private static final String LOG_TAG = ScoresSingleWidget.class.getSimpleName();

    private AppWidgetManager mAppWidgetManager;
    private int[] mAppWidgetIds;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        mAppWidgetManager = appWidgetManager;
        mAppWidgetIds = appWidgetIds;

        Intent myUpdateIntent = new Intent(context, MyWidgetUpdateService.class);
        context.startService(myUpdateIntent);

        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        AppWidgetProviderInfo myAppWidgetProviderInfo = null;
        if (appWidgetManager != null) {
            myAppWidgetProviderInfo = appWidgetManager.getAppWidgetInfo(appWidgetId);
        }

        if (myAppWidgetProviderInfo != null) {
            String label = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                label = myAppWidgetProviderInfo.loadLabel(context.getPackageManager());
            } else {
                label = myAppWidgetProviderInfo.label;
            }


            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.scores_widget_single);
            // views.setTextViewText(R.id.home_name,);
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (MainActivity.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            if (intent.getParcelableExtra(MainActivity.EXTRA_DBDATA) instanceof ScoresEntry
                    && mAppWidgetIds != null && mAppWidgetManager != null) {
                for (int i = 0; i < mAppWidgetIds.length; i++) {
                    updateAppWidget(context, mAppWidgetManager, mAppWidgetIds[i]);
                }
            }
        }
    }
}

