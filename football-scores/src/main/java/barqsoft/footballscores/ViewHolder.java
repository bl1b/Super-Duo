package barqsoft.footballscores;

import android.view.View;
import android.widget.TextView;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ViewHolder {
    public TextView home_name;
    public TextView away_name;
    public TextView score;
    public TextView date;
    public double match_id;

    public ViewHolder(View view) {
        if (view.findViewById(R.id.scores_widget_single_home_name) instanceof TextView) {
            home_name = (TextView) view.findViewById(R.id.scores_widget_single_home_name);
        }

        if (view.findViewById(R.id.scores_widget_single_away_name) instanceof TextView) {
            away_name = (TextView) view.findViewById(R.id.scores_widget_single_away_name);
        }

        if (view.findViewById(R.id.score_textview) instanceof TextView) {
            score = (TextView) view.findViewById(R.id.score_textview);
        }

        if (view.findViewById(R.id.data_textview) instanceof TextView) {
            date = (TextView) view.findViewById(R.id.data_textview);
        }
    }
}
