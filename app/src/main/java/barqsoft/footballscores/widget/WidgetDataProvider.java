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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.ScoresProvider;

/**
 * This acts as the adapter to provide the data to the widget
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    List<String> collection = new ArrayList<>();
    private Cursor data = null;
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
//        for (int i = 1; i < 11; i++) {
//            collection.add("ListView item " + i);
//        }
//        try {
//            // TODO: 19/02/16 provide the actual football data here
//            final long identityToken = Binder.clearCallingIdentity();
//            ScoresProvider scoresProvider = new ScoresProvider();
//            Uri fixtures_by_data = DatabaseContract.scores_table.buildScoreWithDate();
//            data = scoresProvider.query(fixtures_by_data, DB_COLUMNS, null, null,
//                    DatabaseContract.scores_table.DATE_COL + " ASC");
//            Binder.restoreCallingIdentity(identityToken);
//        } catch (Exception e) {
//            Log.i("app", "Error getting data");
//        }
        final long identityToken = Binder.clearCallingIdentity();
        data = context.getContentResolver().query(
                DatabaseContract.scores_table.buildScoreWithDate(), null, null, new String[]{"today"}, null);
        Binder.restoreCallingIdentity(identityToken);

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
