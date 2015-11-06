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

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Class ScoresEntry.
 *
 * [DOCUMENT ME]
 *
 * @author Jan Grünewald
 * @version 1.0.0
 */
public class ScoresEntry implements Parcelable {
    private int mId;
    private int mHomeGoals;
    private int mAwayGoals;
    private int mMatchDay;
    private String mLeague;
    private String mDate;
    private String mTime;
    private String mHomeTeam;
    private String mAwayTeam;

    public ScoresEntry() {
        mId = -1;
        mHomeGoals = -1;
        mAwayGoals = -1;
        mMatchDay = -1;
        mLeague = null;
        mDate = null;
        mTime = null;
        mHomeTeam = null;
        mAwayTeam = null;
    }
    protected ScoresEntry(Parcel in) {
        mId = in.readInt();
        mHomeGoals = in.readInt();
        mAwayGoals = in.readInt();
        mMatchDay = in.readInt();
        mLeague = in.readString();
        mDate = in.readString();
        mTime = in.readString();
        mHomeTeam = in.readString();
        mAwayTeam = in.readString();
    }

    public static final Creator<ScoresEntry> CREATOR = new Creator<ScoresEntry>() {
        @Override
        public ScoresEntry createFromParcel(Parcel in) {
            return new ScoresEntry(in);
        }

        @Override
        public ScoresEntry[] newArray(int size) {
            return new ScoresEntry[size];
        }
    };

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mHomeGoals);
        dest.writeInt(mAwayGoals);
        dest.writeInt(mMatchDay);
        dest.writeString(mLeague);
        dest.writeString(mDate);
        dest.writeString(mTime);
        dest.writeString(mHomeTeam);
        dest.writeString(mAwayTeam);
    }

    public int getId() {
        return mId;
    }

    public void setId(int pId) {
        mId = pId;
    }

    public int getHomeGoals() {
        return mHomeGoals;
    }

    public void setHomeGoals(int pHomeGoals) {
        mHomeGoals = pHomeGoals;
    }

    public int getAwayGoals() {
        return mAwayGoals;
    }

    public void setAwayGoals(int pAwayGoals) {
        mAwayGoals = pAwayGoals;
    }

    public int getMatchDay() {
        return mMatchDay;
    }

    public void setMatchDay(int pMatchDay) {
        mMatchDay = pMatchDay;
    }

    public String getLeague() {
        return mLeague;
    }

    public void setLeague(String pLeague) {
        mLeague = pLeague;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String pDate) {
        mDate = pDate;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String pTime) {
        mTime = pTime;
    }

    public String getHomeTeam() {
        return mHomeTeam;
    }

    public void setHomeTeam(String pHomeTeam) {
        mHomeTeam = pHomeTeam;
    }

    public String getAwayTeam() {
        return mAwayTeam;
    }

    public void setAwayTeam(String pAwayTeam) {
        mAwayTeam = pAwayTeam;
    }
}
