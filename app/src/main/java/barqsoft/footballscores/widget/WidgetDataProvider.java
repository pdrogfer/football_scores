package barqsoft.footballscores.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

/**
 * This acts as the adapter to provide the data to the widget
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    List<String> collection = new ArrayList<>();
    Context context;
    Intent intent;


    public WidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    private void initData() {
        collection.clear();
        // TODO: 19/02/16 provide the actual football data here
        for (int i = 1; i < 11; i++) {
            collection.add("ListView item " + i);
        }
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
