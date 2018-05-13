package com.bf.bakingapp.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bf.bakingapp.R;
import com.bf.bakingapp.adapter.IngredientsAdapter;
import com.bf.bakingapp.model.Ingredient;
import com.bf.bakingapp.model.Recipe;
import com.bf.bakingapp.viewmodel.ViewModelRecipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsActivity extends AppCompatActivity {

    private static final String TAG = IngredientsActivity.class.getSimpleName();
    public final static String KEY_RECIPE = "key_recipe";

    //private Recipe mRecipe;
    ViewModelRecipe mViewModel;

    @BindView(R.id.lv_ingredients)
    ListView mLvIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.ingredients));

        mViewModel = ViewModelProviders.of(this).get(ViewModelRecipe.class);
        if (mViewModel.getRecipe() == null){
            if (getIntent().hasExtra(KEY_RECIPE)) {
                mViewModel.setRecipe((Recipe)getIntent().getSerializableExtra(KEY_RECIPE));
                Log.d(TAG, "onCreate: Has INTENT. Recipe is "+mViewModel.getRecipe()==null?"NULL":"NOT NULL");
            }
        }

        if (mViewModel.getRecipe() != null){
            fillList((ArrayList<Ingredient>) mViewModel.getRecipe().getIngredients());
        }
    }

    private void fillList(ArrayList<Ingredient> ingredients){
        IngredientsAdapter adapter = new IngredientsAdapter(this, ingredients);
        mLvIngredients.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

}
