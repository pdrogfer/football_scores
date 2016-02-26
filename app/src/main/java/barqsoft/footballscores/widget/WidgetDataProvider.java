package barqsoft.footballscores.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.ScoresAdapter;
import barqsoft.footballscores.Utilities;

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
        Uri fixturesByData = DatabaseContract.scores_table.buildScoreWithDate();
        final long identityToken = Binder.clearCallingIdentity();
        // I am getting the matches for today and yesterday only
        dataToday = context.getContentResolver().query(
                fixturesByData, null, null, new String[]{today}, null);
        dataYesterday = context.getContentResolver().query(
                fixturesByData, null, null, new String[]{yesterday}, null);
        Binder.restoreCallingIdentity(identityToken);
        if (dataToday.getCount() <= 0 && dataYesterday.getCount() <= 0) {
            Log.i(TAG, "initData: no data");
        }

        while (dataToday.moveToNext()) {
            addData(dataToday,  collection);
        }
        while (dataYesterday.moveToNext()) {
            addData(dataYesterday,  collection);
        }
    }

    private void addData(Cursor data, List<String> collection) {

        String teamHome = data.getString(ScoresAdapter.COL_HOME);
        String teamAway = data.getString(ScoresAdapter.COL_AWAY);
        String score = Utilities.getScores(data.getInt(ScoresAdapter.COL_HOME_GOALS),
                data.getInt(ScoresAdapter.COL_AWAY_GOALS));
        String tempText = teamHome + "," + teamAway + "," + score;
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
        String fixtureData = collection.get(position);
        Log.i(TAG, "getViewAt: fixtureData = " + fixtureData);
        String homeTeam = fixtureData.substring(0, fixtureData.indexOf(","));
        String awayTeam = fixtureData.substring(fixtureData.indexOf(",") + 1, fixtureData.lastIndexOf(","));
        String result = fixtureData.substring(fixtureData.lastIndexOf(",") + 1);
        Log.i(TAG, "getViewAt: result = " + result);
        RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                R.layout.widget_item);
        remoteView.setTextViewText(R.id.widget_item_team_home, homeTeam);
        remoteView.setTextViewText(R.id.widget_item_team_away, awayTeam);
        remoteView.setTextViewText(R.id.widget_item_result, result);
        remoteView.setTextColor(R.id.widget_item_team_home, Color.BLACK);
        remoteView.setTextColor(R.id.widget_item_team_away, Color.BLACK);
        remoteView.setTextColor(R.id.widget_item_result, Color.BLACK);

        // widget list items click open MainActivity
        Bundle extras = new Bundle();
        extras.putInt(Widget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteView.setOnClickFillInIntent(R.id.widget_item_layout, fillInIntent);

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
