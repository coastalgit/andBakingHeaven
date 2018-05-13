package com.bf.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.bf.bakingapp.R;
import com.bf.bakingapp.manager.RecipeManager;

public class BakingWidgetProvider extends AppWidgetProvider {

    //private static final String TAG = BakingWidgetProvider.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);
        Intent intent = new Intent(context, BakingStepsWidgetService.class);
        views.setRemoteAdapter(R.id.lv_widget, intent);

        if (RecipeManager.getInstance().getRecipe() != null)
            views.setTextViewText(R.id.tv_widget_title, RecipeManager.getInstance().getRecipe().getName());

        appWidgetManager.updateAppWidget(appWidgetId, views);
   }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            ComponentName appWidget = new ComponentName(context, BakingWidgetProvider.class);
            int[] appWidgetIds = widgetManager.getAppWidgetIds(appWidget);
            onUpdate(context, widgetManager, appWidgetIds);
            widgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_widget);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        //
    }

    @Override
    public void onDisabled(Context context) {
        //
    }
}

