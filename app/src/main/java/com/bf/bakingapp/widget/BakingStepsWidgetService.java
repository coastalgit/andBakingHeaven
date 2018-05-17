package com.bf.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bf.bakingapp.R;
import com.bf.bakingapp.manager.RecipeManager;
import com.bf.bakingapp.model.Ingredient;

import java.util.ArrayList;

public class BakingStepsWidgetService extends RemoteViewsService {

    private static final String TAG = BakingStepsWidgetService.class.getSimpleName();

    public BakingStepsWidgetService() {
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingStepsRemoteViewsFactory(this.getApplicationContext());
    }

    class BakingStepsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext;
        //String mRecipeHeader = "";
        ArrayList<Ingredient> mIngredientList = new ArrayList<>();

        public BakingStepsRemoteViewsFactory(Context appContext) {
            this.mContext = appContext;
            updateRecipeData();
        }

        private void updateRecipeData(){
            //mRecipeHeader = RecipeManager.getInstance().getRecipe().getName();

            if (RecipeManager.getInstance().getRecipe() != null) {
                if (RecipeManager.getInstance().getRecipe().getIngredients().size() > 0)
                    mIngredientList = (ArrayList<Ingredient>) RecipeManager.getInstance().getRecipe().getIngredients();
            }
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            updateRecipeData();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mIngredientList == null)
                return 0;
            return mIngredientList.size();
        }

        @Override
        public RemoteViews getViewAt(int pos) {
            if (pos == AdapterView.INVALID_POSITION || mIngredientList == null)
                return null;

            Ingredient ingred = mIngredientList.get(pos);
            final RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.list_item_ingredient);

            String line1 = ingred.getQuantity() + " (" + ingred.getMeasure() + ")";
            rv.setTextViewText(R.id.tv_ingredient_measure, line1);
            rv.setTextViewText(R.id.tv_ingredient_text, ingred.getIngredient());

            return rv;

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
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}