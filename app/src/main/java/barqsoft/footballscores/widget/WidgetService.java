package barqsoft.footballscores.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by pedro on 16/02/16.
 */
public class WidgetService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        // return remote view factory here
        return new WidgetDataProvider(this, intent);
    }
}
