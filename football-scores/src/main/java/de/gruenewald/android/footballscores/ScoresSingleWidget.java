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

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.service.MyWidgetUpdateService;

/**
 * Implementation of App Widget functionality.
 */
public class ScoresSingleWidget extends AppWidgetProvider {
    private static final String LOG_TAG = ScoresSingleWidget.class.getSimpleName();

    private ScoresEntry currentWidgetData;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //IntentService will fetch current data and then invoke a broadcast which is received by the onReceive()
        //method
        Intent myUpdateIntent = new Intent(context, MyWidgetUpdateService.class);
        context.startService(myUpdateIntent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void updateAppWidget(Context context, ScoresEntry widgetData) {
        AppWidgetManager myManager = AppWidgetManager.getInstance(context);
        if (myManager != null && widgetData != null) {

            ComponentName myComponentName = new ComponentName(context.getPackageName(), ScoresSingleWidget.class.getName());
            //there may be several widgets to update; so we have to fetch the ids
            int[] myAppWidgetIds = myManager.getAppWidgetIds(myComponentName);

            Intent mainIntent = new Intent(context, MainActivity.class);
            PendingIntent startIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);
            //fetch the remoteview (layout) for the widget, set its properties and update
            String homeGoals = Integer.toString(Math.max(0, widgetData.getHomeGoals()));
            String awayGoals = Integer.toString(Math.max(0, widgetData.getAwayGoals()));
            String scoreString = homeGoals.concat(" - ").concat(awayGoals);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.scores_widget_single);
            views.setOnClickPendingIntent(R.id.layout_scores_widget_single, startIntent);
            views.setTextViewText(R.id.scores_widget_single_home_name, widgetData.getHomeTeam());
            views.setTextViewText(R.id.scores_widget_single_score, scoreString);
            views.setTextViewText(R.id.scores_widget_single_away_name, widgetData.getAwayTeam());

            for (int i = 0; i < myAppWidgetIds.length; i++) {
                // Instruct the widget manager to update the widget
                myManager.updateAppWidget(myAppWidgetIds[i], views);
            }
        }
    }

    /**
     * Receives broadcasts.
     * Broadcast will be send when data has been updated.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (MainActivity.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            if (intent.getParcelableExtra(MainActivity.EXTRA_DBDATA) instanceof ScoresEntry) {
                updateAppWidget(context, (ScoresEntry) intent.getParcelableExtra(MainActivity.EXTRA_DBDATA));
            }
        }
    }
}

