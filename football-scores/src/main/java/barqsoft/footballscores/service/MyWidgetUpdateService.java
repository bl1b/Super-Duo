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

package barqsoft.footballscores.service;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import de.gruenewald.android.footballscores.ScoresEntry;

/**
 * Class MyWidgetUpdateService.
 *
 * [DOCUMENT ME]
 *
 * @author Jan Grünewald
 * @version 1.0.0
 */
public class MyWidgetUpdateService extends IntentService {
    private static final String SERVICE_NAME = MyWidgetUpdateService.class.getSimpleName();

    public MyWidgetUpdateService() {
        this(SERVICE_NAME);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyWidgetUpdateService(String name) {
        super(name);
    }

    @Override protected void onHandleIntent(Intent intent) {
        String[] mySelectionArguments = new String[1];

        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat mySimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        mySelectionArguments[0] = mySimpleDateFormat.format(now);
        // fetch the latest match from today
        Cursor myDataCursor = getContentResolver().query(
                DatabaseContract.scores_table.buildScoreWithDate(),
                null, DatabaseContract.scores_table.MATCH_DAY + "= ?", mySelectionArguments,
                DatabaseContract.scores_table.MATCH_ID + " DESC"
        );

        if (myDataCursor != null && myDataCursor.getCount() > 0 && myDataCursor.moveToFirst()) {
            Log.i(SERVICE_NAME, "Data retrieved.");
            ScoresEntry myDbEntry = new ScoresEntry();
            myDbEntry.setId(myDataCursor.getInt(myDataCursor.getColumnIndex(DatabaseContract.scores_table.MATCH_ID)));
            myDbEntry.setHomeGoals(myDataCursor.getInt(myDataCursor.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL)));
            myDbEntry.setAwayGoals(myDataCursor.getInt(myDataCursor.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL)));
            myDbEntry.setMatchDay(myDataCursor.getInt(myDataCursor.getColumnIndex(DatabaseContract.scores_table.MATCH_DAY)));
            myDbEntry.setLeague(myDataCursor.getString(myDataCursor.getColumnIndex(DatabaseContract.scores_table.LEAGUE_COL)));
            myDbEntry.setDate(myDataCursor.getString(myDataCursor.getColumnIndex(DatabaseContract.scores_table.DATE_COL)));
            myDbEntry.setTime(myDataCursor.getString(myDataCursor.getColumnIndex(DatabaseContract.scores_table.TIME_COL)));
            myDbEntry.setHomeTeam(myDataCursor.getString(myDataCursor.getColumnIndex(DatabaseContract.scores_table.HOME_COL)));
            myDbEntry.setAwayTeam(myDataCursor.getString(myDataCursor.getColumnIndex(DatabaseContract.scores_table.AWAY_COL)));

            Intent myWidgetDataIntent = new Intent(MainActivity.ACTION_DATA_UPDATED);
            myWidgetDataIntent.putExtra(MainActivity.EXTRA_DBDATA, myDbEntry);
            sendBroadcast(myWidgetDataIntent);
        } else {
            Log.w(SERVICE_NAME, "There are not results in the cursor.");
        }
    }
}
