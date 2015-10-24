package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.widget.CursorAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.gruenewald.android.footballscores.ScoresHelper;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ScoresAdapter extends CursorAdapter {
    private static final String LOG_TAG = ScoresAdapter.class.getSimpleName();

    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;
    public double detail_match_id = 0;

    private Context mContext;

    public ScoresAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View mItem = LayoutInflater.from(context).inflate(R.layout.scores_list_item, parent, false);
        ViewHolder mHolder = new ViewHolder(mItem);
        mItem.setTag(mHolder);
        //Log.v(FetchScoreTask.LOG_TAG,"new View inflated");
        return mItem;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder mHolder = (ViewHolder) view.getTag();
        // Edit JG: handle home/away-team text+icon combination as compound drawable to increase
        // performance
        if (mHolder.home_name != null) {
            mHolder.home_name.setText(cursor.getString(COL_HOME));
            Drawable myHomeCrest;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                myHomeCrest = context.getDrawable(Utilies.getTeamCrestByTeamName(cursor.getString(COL_HOME)));
            } else {
                // we can suppress the deprecation here because we need to support API Level 10
                // the non-deprecated method to use was added in LOLLIPOP
                //noinspection deprecation
                myHomeCrest = context.getResources().getDrawable(Utilies.getTeamCrestByTeamName(cursor.getString(COL_HOME)));
            }
            if (myHomeCrest != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    mHolder.home_name.setCompoundDrawablesWithIntrinsicBounds(null, myHomeCrest, null, null);
                } else {
                    // TODO: call setBounds on drawable with correct dimensions
                    mHolder.home_name.setCompoundDrawables(null, myHomeCrest, null, null);
                }
            }
        }

        if (mHolder.away_name != null) {
            mHolder.away_name.setText(cursor.getString(COL_AWAY));
            Drawable myAwayCrest;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                myAwayCrest = context.getDrawable(Utilies.getTeamCrestByTeamName(cursor.getString(COL_AWAY)));
            } else {
                // we can suppress the deprecation here because we need to support API Level 10
                // the non-deprecated method to use was added in LOLLIPOP
                //noinspection deprecation
                myAwayCrest = context.getResources().getDrawable(Utilies.getTeamCrestByTeamName(cursor.getString(COL_AWAY)));
            }
            if (myAwayCrest != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    mHolder.away_name.setCompoundDrawablesWithIntrinsicBounds(null, myAwayCrest, null, null);
                } else {
                    // TODO: call setBounds on drawable with correct dimensions
                    mHolder.away_name.setCompoundDrawables(null, myAwayCrest, null, null);
                }
            }
        }

        if (mHolder.date != null) {
            mHolder.date.setText(cursor.getString(COL_MATCHTIME));
        }

        if (mHolder.score != null) {
            mHolder.score.setText(Utilies.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS)));
        }

        mHolder.match_id = cursor.getDouble(COL_ID);

        // Edit JG: always remove childs from fragment_container to avoid potential overlapping
        // elements
        ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container);
        container.removeAllViews();

        if (mHolder.match_id == detail_match_id) {
            //Log.v(FetchScoreTask.LOG_TAG,"will insert extraView");
            // Edit JG: only instantiate a LayoutInflater and inflate a layout if it's really
            // required
            LayoutInflater vi = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.detail_fragment, null);

            // setup the Matchday-Label
            TextView match_day = (TextView) v.findViewById(R.id.matchday_textview);
            match_day.setText(Utilies.getMatchDay(cursor.getInt(COL_MATCHDAY), cursor.getInt(COL_LEAGUE)));

            // setup the League-Label
            TextView league = (TextView) v.findViewById(R.id.league_textview);
            league.setText(Utilies.getLeague(cursor.getInt(COL_LEAGUE), context));

            // setup the Share-Button
            // Edit JG: setup the image/text combination for the Share-Button as Spannable
            // because "centering" the drawableLeft with padding makes it depending on the
            // resolution and will look terrible on wide screens
            if (v.findViewById(R.id.share_button) instanceof Button) {
                Button share_button = (Button) v.findViewById(R.id.share_button);
                //share_button.setTransformationMethod(null);
                share_button.setText(ScoresHelper.createLeftImageSpannable(
                        context,
                        R.string.share_text,
                        R.drawable.abc_ic_menu_share_mtrl_alpha
                ));
                share_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //add Share Action
                        context.startActivity(createShareForecastIntent(mHolder.home_name.getText() + " "
                                + mHolder.score.getText() + " " + mHolder.away_name.getText() + " "));
                    }
                });
                share_button.invalidate();
            }
            container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            container.removeAllViews();
        }
    }

    public Intent createShareForecastIntent(String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        // Edit JG: FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET is deprecated since API 21
        // only use this flag on versions prior to 21
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //noinspection deprecation
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        } else {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }

        shareIntent.setType("text/plain");
        // Edit JG: replaced static hashtag-string with entry from strings.xml
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + mContext.getString(R.string.football_scores_hashtag));
        return shareIntent;
    }

}
