package com.bf.bakingapp.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bf.bakingapp.R;
import com.bf.bakingapp.adapter.RecipeAdapter;
import com.bf.bakingapp.manager.RecipeManager;
import com.bf.bakingapp.model.Recipe;
import com.bf.bakingapp.utility.NetworkUtils;
import com.bf.bakingapp.utility.SimpleIdlingResource;
import com.bf.bakingapp.viewmodel.ViewModelMain;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    ViewModelMain mRecipesViewModel;
    NetworkUtils mNetworkUtils;
    RecipeAdapter mRecipeAdapter;

    @BindView(R.id.layout_main_error)
    LinearLayout mLayoutError;
    @BindView(R.id.tv_main_label)
    TextView mMainLabel;
    @BindView(R.id.recyclerview_recipes)
    RecyclerView mRecyclerViewRecipes;

    //region Espresso
    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
    //endregion Espresso

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mNetworkUtils = new NetworkUtils();

        getIdlingResource();
        if (mIdlingResource != null)
            mIdlingResource.setIdleState(false);

        mRecipeAdapter = new RecipeAdapter(this, this);
        mRecyclerViewRecipes.setAdapter(mRecipeAdapter);
        mRecyclerViewRecipes.setHasFixedSize(true);
        boolean isTablet = getResources().getBoolean(R.bool.is_landscape);
        applyLayoutManager(isTablet);

        attachViewModel();

        if (!mNetworkUtils.isConnected(this)) {
            String errorStr = getString(R.string.availablenot) + " (" + getString(R.string.connection) +")";
            displayErrorMessage(true, errorStr);
        }
    }


    private void attachViewModel() {
        mRecipesViewModel = ViewModelProviders.of(this).get(ViewModelMain.class);
        subscribe();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    private void subscribe() {
        mRecipesViewModel.getRecipesObservable().observe(this, new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Recipe> recipes) {
                reloadRecipeAdapter(recipes);
            }
        });
    }

    private void applyLayoutManager(boolean asGrid) {
        if (asGrid) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            mRecyclerViewRecipes.setLayoutManager(gridLayoutManager);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerViewRecipes.setLayoutManager(linearLayoutManager);
        }
        // TODO: 24/04/2018 Divider
        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_line));
        mRecyclerViewRecipes.addItemDecoration(divider);
    }

    private void reloadRecipeAdapter(final ArrayList<Recipe> recipes){

//        if (mNetworkUtils.isConnected(this)) {
            if (recipes != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayErrorMessage(false,"");
                        mRecipeAdapter.reloadAdapter(recipes);

                        if (mIdlingResource != null)
                            mIdlingResource.setIdleState(true);
                    }
                });
            }
//        }
//        else{
//            String errorStr = getString(R.string.availablenot) + " (" + getString(R.string.connection) +")";
//            displayErrorMessage(true, errorStr);
//        }
    }

    private void displayErrorMessage(boolean show, String msg){
        mRecyclerViewRecipes.setVisibility(show?View.INVISIBLE:View.VISIBLE);
        mLayoutError.setVisibility(show?View.VISIBLE:View.INVISIBLE);
        mMainLabel.setText(msg);
    }

    private void loadRecipeDetails(Recipe recipe){
        Intent recipeIntent = new Intent(this, RecipeActivity.class);
        recipeIntent.putExtra(RecipeActivity.KEY_RECIPE, recipe);
        startActivity(recipeIntent);
    }

    private void updateWidgetStorageWithIngredients(Recipe recipe){
        RecipeManager.getInstance().setRecipe(recipe);
        // TODO: 01/05/2018 Save to disk with (app) context
    }

    @OnClick(R.id.btn_retry)
    public void btnRetry_onClick(Button btn){
        subscribe();
        mRecipesViewModel.getRecipesFromServer();
    }

    @Override
    public void onClick(Recipe recipe) {
        Log.d(TAG, "onClick: Recipe:["+recipe.getId()+"]");
        updateWidgetStorageWithIngredients(recipe);
        loadRecipeDetails(recipe);
    }

}
