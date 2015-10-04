package barqsoft.footballscores;

import android.content.Context;

import de.gruenewald.android.footballscores.LeagueCodes;

/**
 * Created by yehya khaled on 3/3/2015.
 * Modified by jan.gruenewald84@googlemail.com on 04/10/2015.
 */
public class Utilies {
    public static String getLeague(int league_num, Context pContext) {
        // Edit JG: fetching strings from strings.xml instead of static strings
        // setting value of return value instead of returning directly in switch (bad style)
        String myLeagueName;
        switch (league_num) {
            case LeagueCodes.SERIE_A:
                myLeagueName = pContext.getString(R.string.seriaa);
                break;
            case LeagueCodes.PREMIER_LEAGUE:
                myLeagueName = pContext.getString(R.string.premierleague);
                break;
            case LeagueCodes.CHAMPIONS_LEAGUE:
                myLeagueName = pContext.getString(R.string.champions_league);
                break;
            case LeagueCodes.PRIMERA_DIVISION:
                myLeagueName = pContext.getString(R.string.primeradivison);
                break;
            case LeagueCodes.BUNDESLIGA1:
                myLeagueName = pContext.getString(R.string.bundesliga);
                break;
            default:
                myLeagueName = pContext.getString(R.string.error_unknown_league);
        }

        return myLeagueName;
    }

    public static String getMatchDay(int match_day, int league_num) {
        if (league_num == LeagueCodes.CHAMPIONS_LEAGUE) {
            if (match_day <= 6) {
                return "Group Stages, Matchday : 6";
            } else if (match_day == 7 || match_day == 8) {
                return "First Knockout round";
            } else if (match_day == 9 || match_day == 10) {
                return "QuarterFinal";
            } else if (match_day == 11 || match_day == 12) {
                return "SemiFinal";
            } else {
                return "Final";
            }
        } else {
            return "Matchday : " + String.valueOf(match_day);
        }
    }

    public static String getScores(int home_goals, int awaygoals) {
        if (home_goals < 0 || awaygoals < 0) {
            return " - ";
        } else {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName(String teamname) {
        if (teamname == null) {
            return R.drawable.no_icon;
        }
        switch (teamname) { //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.
            case "Arsenal London FC":
                return R.drawable.arsenal;
            case "Manchester United FC":
                return R.drawable.manchester_united;
            case "Swansea City":
                return R.drawable.swansea_city_afc;
            case "Leicester City":
                return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC":
                return R.drawable.everton_fc_logo1;
            case "West Ham United FC":
                return R.drawable.west_ham;
            case "Tottenham Hotspur FC":
                return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion":
                return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC":
                return R.drawable.sunderland;
            case "Stoke City FC":
                return R.drawable.stoke_city;
            default:
                return R.drawable.no_icon;
        }
    }
}
