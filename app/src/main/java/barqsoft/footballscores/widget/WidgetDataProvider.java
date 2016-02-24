package barqsoft.footballscores.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.ScoresAdapter;

/**
 * This acts as the adapter to provide the dataToday to the widget
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "football_scores";
    List<String> collection = new ArrayList<>();
    private Cursor dataToday = null;
    private Cursor dataYesterday = null;
    Context context;
    Intent intent;
    final String[] DB_COLUMNS = {
            DatabaseContract.scores_table.LEAGUE_COL,
            DatabaseContract.scores_table.DATE_COL,
            DatabaseContract.scores_table.TIME_COL,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.MATCH_ID,
            DatabaseContract.scores_table.MATCH_DAY
    };


    public WidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    private void initData() {
        collection.clear();
        String format = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        String today = simpleDateFormat.format(new Date());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = simpleDateFormat.format(cal.getTime());
        Log.i(TAG, "initData: dateformat = " + today);
        Uri fixturesByData = DatabaseContract.scores_table.buildScoreWithDate();
        final long identityToken = Binder.clearCallingIdentity();
        // TODO: 24/02/16 that does not seem an elegant solution to me. Must be a better way 
        dataToday = context.getContentResolver().query(
                fixturesByData, DB_COLUMNS, null, new String[]{today}, null);
        dataYesterday = context.getContentResolver().query(
                fixturesByData, DB_COLUMNS, null, new String[]{yesterday}, null);
        Binder.restoreCallingIdentity(identityToken);
        if (dataToday.getCount() <= 0 && dataYesterday.getCount() <= 0) {
            Log.i(TAG, "initData: no data");
        } else {
            Log.i(TAG, "initData: data ok");
        }
        String tempText = null;
        while (dataToday.moveToNext()) {
            addData(dataToday, tempText, collection);
        }
        while (dataYesterday.moveToNext()) {
            addData(dataYesterday, tempText, collection);
        }
    }

    private void addData(Cursor data, String tempText, List<String> collection) {
        // TODO: 24/02/16 transform collection in a bi-dimensional array to store home, away and result
        String teamHome = data.getString(ScoresAdapter.COL_HOME);
        String teamAway = data.getString(ScoresAdapter.COL_AWAY);
        String goalsHome = data.getString(ScoresAdapter.COL_HOME_GOALS);
        String goalsAway = data.getString(ScoresAdapter.COL_AWAY_GOALS);
        //String teamHome = data.getString(ScoresAdapter.COL_HOME);
        tempText = teamHome + " - " + teamAway;
        collection.add(tempText);
    }


    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return collection.size();
    }

//    populate each view in widget dynamically
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                android.R.layout.simple_list_item_1);
        remoteView.setTextViewText(android.R.id.text1, collection.get(position));
        remoteView.setTextColor(android.R.id.text1, Color.BLACK);
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
