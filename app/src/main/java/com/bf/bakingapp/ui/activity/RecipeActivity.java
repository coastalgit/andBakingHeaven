package com.bf.bakingapp.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bf.bakingapp.R;
import com.bf.bakingapp.model.Recipe;
import com.bf.bakingapp.model.Step;
import com.bf.bakingapp.ui.fragments.RecipeInstructionFragment;
import com.bf.bakingapp.ui.fragments.RecipeStepsFragment;
import com.bf.bakingapp.viewmodel.ViewModelRecipe;

public class RecipeActivity extends AppCompatActivity implements
        RecipeStepsFragment.OnStepsFragmentInteractionListener,
        RecipeInstructionFragment.OnInstructionFragmentInteractionListener{

    private static final String TAG = RecipeActivity.class.getSimpleName();
    public final static String KEY_RECIPE = "key_recipe";
    private static final String FRAGMENT_STEPS = "key_steps";
    private static final String FRAGMENT_STEPS_TAG = "key_stepstag";
    private static final String FRAGMENT_INSTRUCTION = "key_instruct";
    private static final String FRAGMENT_INSTRUCTION_TAG = "key_instructtag";

    ViewModelRecipe mViewModel;

    //private Recipe mRecipe;
    private boolean mIsLandscape = false;
    private boolean mIsTwoPane = false;

    TextView mTvRecipeStepTitle;
    TextView mTvRecipeInstructionTitle;

    Fragment mFragmentSteps;
    Fragment mFragmentInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_recipe);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mViewModel = ViewModelProviders.of(this).get(ViewModelRecipe.class);
        if (mViewModel.getRecipe() == null){
            if (getIntent().hasExtra(KEY_RECIPE)) {
                mViewModel.setRecipe((Recipe)getIntent().getSerializableExtra(KEY_RECIPE));
                Log.d(TAG, "onCreate: Has INTENT. Recipe is "+mViewModel.getRecipe()==null?"NULL":"NOT NULL");
            }
        }

        mIsTwoPane = getResources().getBoolean(R.bool.is_tabletsize);
        mIsLandscape = getResources().getBoolean(R.bool.is_landscape);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            Log.d(TAG, "onCreate: NO INSTANCE");
            mFragmentSteps = RecipeStepsFragment.newInstance(mViewModel.getRecipe());
            fragmentManager.beginTransaction()
                    .add(R.id.layout_main_steps, mFragmentSteps, FRAGMENT_STEPS_TAG)
                    .commit();

            if (mIsTwoPane) {
                mFragmentInstructions = RecipeInstructionFragment.newInstance(mViewModel.getRecipe());
                fragmentManager.beginTransaction()
                        .add(R.id.layout_main_instructions, mFragmentInstructions, FRAGMENT_INSTRUCTION_TAG)
                        .commit();
                // TODO: 25/04/2018 Cater for saves step
                //Step step = mViewModel.getRecipe().getSteps().get(0);
                //((RecipeInstructionFragment)mFragmentInstructions).updateInstruction(step);
            }
        }
        else{
            Log.d(TAG, "onCreate: HAVE INSTANCE");

            mFragmentSteps = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_STEPS);
            if (mIsTwoPane)
                mFragmentInstructions = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_INSTRUCTION);
        }

// TODO: 26/04/2018 save psotion, and update instruction view (including hiding of relevant back/fwd arrows

        //buildView();

        if (mViewModel.getRecipe() != null) {
            Log.d(TAG, "onCreate: Recipe selected:[" + mViewModel.getRecipe().getName() + "]");
            getSupportActionBar().setTitle(mViewModel.getRecipe().getName());
            populatePreliminaryFields(mViewModel.getRecipe());
        } else {
            // TODO: 25/04/2018 Display error
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);

        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_STEPS_TAG) != null)
            getSupportFragmentManager().putFragment(outState, FRAGMENT_STEPS, mFragmentSteps);

        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_INSTRUCTION_TAG) != null)
            getSupportFragmentManager().putFragment(outState, FRAGMENT_INSTRUCTION, mFragmentInstructions);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart: ");
        super.onRestart();
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

    private void buildView(){
        //determine layout for device
//        mIsTwoPane = getResources().getBoolean(R.bool.is_tabletsize);
//        mIsLandscape = getResources().getBoolean(R.bool.is_landscape);

//        mTvRecipeStepTitle = findViewById(R.id.tv_steps_title);
//        if (mIsTwoPane){
//            mTvRecipeStepTitle = findViewById(R.id.tv_instruction_title);
//        }
    }

    private void populatePreliminaryFields(Recipe recipe){
//        mTvRecipeStepTitle.setText(recipe.getName());
//        if (mTvRecipeInstructionTitle != null)
//            mTvRecipeInstructionTitle.setText("INSTRUCTIONS FOR "+ recipe.getName());
    }

    public void onRecipeStepSelected(int position){
        Log.d(TAG, "onRecipeStepSelected: pos="+String.valueOf(position));
    }

    @Override
    public void onFragmentStepNav_Back() {
        int current_pos = ((RecipeStepsFragment)mFragmentSteps).getStepsAdapter().getSelectedPosition();
        Log.d(TAG, "onFragmentStepNav_Back: current pos:"+String.valueOf(current_pos));
        if (current_pos > 0)
            ((RecipeStepsFragment)mFragmentSteps).highlightActiveStep(current_pos-1);
    }

    @Override
    public void onFragmentStepNav_Forward() {
        int current_pos = ((RecipeStepsFragment)mFragmentSteps).getStepsAdapter().getSelectedPosition();
        Log.d(TAG, "onFragmentStepNav_Forward: current pos:"+String.valueOf(current_pos));
        if (current_pos < mViewModel.getRecipe().getSteps().size())
            ((RecipeStepsFragment)mFragmentSteps).highlightActiveStep(current_pos+1);

    }

    @Override
    public void onStepClick(Step step) {
        Log.d(TAG, "onStepClick: step:["+step.getShortDescription()+"]");
        updateInstructionPane(step);
    }

    private void updateInstructionPane(Step step){
        if (mIsTwoPane){
            if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_INSTRUCTION_TAG) != null) {
                ((RecipeInstructionFragment)mFragmentInstructions).updateInstruction(step);
            }
        }
        else{
            // TODO: 25/04/2018
            // new activitry?
        }
    }
}
