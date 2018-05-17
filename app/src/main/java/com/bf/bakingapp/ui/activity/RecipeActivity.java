package com.bf.bakingapp.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bf.bakingapp.R;
import com.bf.bakingapp.model.Recipe;
import com.bf.bakingapp.model.Step;
import com.bf.bakingapp.ui.fragments.RecipeInstructionFragment;
import com.bf.bakingapp.ui.fragments.RecipeStepsFragment;
import com.bf.bakingapp.viewmodel.ViewModelRecipe;
import com.bf.bakingapp.widget.BakingWidgetProvider;

public class RecipeActivity extends AppCompatActivity implements
        RecipeStepsFragment.OnStepsFragmentInteractionListener,
        RecipeInstructionFragment.OnInstructionFragmentInteractionListener{

    private static final String TAG = RecipeActivity.class.getSimpleName();
    public final static String KEY_RECIPE = "key_recipe";
    private static final String FRAGMENT_STEPS = "key_steps";
    private static final String FRAGMENT_STEPS_TAG = "key_stepstag";
    private static final String FRAGMENT_INSTRUCTION = "key_instruct";
    private static final String FRAGMENT_INSTRUCTION_TAG = "key_instructtag";

    public ViewModelRecipe getViewModel() {
        return mViewModel;
    }

    ViewModelRecipe mViewModel;

    //private Recipe mRecipe;
    private boolean mIsLandscape = false;
    private boolean mIsTwoPane = false;

    TextView mTvRecipeStepTitle;
    TextView mTvRecipeInstructionTitle;

    //Toolbar mToolbar;
    FragmentManager mFragmentManager;
    Fragment mFragmentSteps;
    Fragment mFragmentInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_recipe);

        //mToolbar = (Toolbar)findViewById(R.id)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mViewModel = ViewModelProviders.of(this).get(ViewModelRecipe.class);
        if (mViewModel.getRecipe() == null){
            if (getIntent().hasExtra(KEY_RECIPE)) {
                mViewModel.setRecipe((Recipe)getIntent().getSerializableExtra(KEY_RECIPE));
                Log.d(TAG, "onCreate: Has INTENT. Recipe is "+mViewModel.getRecipe()==null?"NULL":"NOT NULL");
            }
        }
        else{
            Log.d(TAG, "onCreate: VM has recipe");
        }

        mIsTwoPane = getResources().getBoolean(R.bool.is_tabletsize);
        mIsLandscape = getResources().getBoolean(R.bool.is_landscape);

        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            Log.d(TAG, "onCreate: NO INSTANCE");
            //mFragmentSteps = RecipeStepsFragment.newInstance(mViewModel.getRecipe());
            //mFragmentManager.beginTransaction()
            //        .add(R.id.layout_main_steps, mFragmentSteps, FRAGMENT_STEPS_TAG)
            //        .commit();
            applyFragment(mFragmentSteps, R.id.layout_main_steps, FRAGMENT_STEPS_TAG);

            mFragmentInstructions = RecipeInstructionFragment.newInstance(mViewModel.getRecipe());
            if (mIsTwoPane) {
//                mFragmentManager.beginTransaction()
//                        .add(R.id.layout_main_instructions, mFragmentInstructions, FRAGMENT_INSTRUCTION_TAG)
//                        .commit();

                applyFragment(mFragmentInstructions, R.id.layout_main_instructions, FRAGMENT_INSTRUCTION_TAG);

                // TODO: 25/04/2018 Cater for saves step
                //Step step = mViewModel.getRecipe().getSteps().get(0);
                //((RecipeInstructionFragment)mFragmentInstructions).updateInstruction(step);
            }

            updateWidgetWithBroadcast();
        }
        else{
            Log.d(TAG, "onCreate: HAVE INSTANCE");

            mFragmentSteps = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_STEPS);
            //if (mIsTwoPane) {
                mFragmentInstructions = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_INSTRUCTION);
            //}
            //((RecipeStepsFragment)mFragmentSteps).highlightActiveStep(mViewModel.getStepActive().getId());
            //onStepClick(mViewModel.getStepActive());
        }

// TODO: 26/04/2018 save psotion, and update instruction view (including hiding of relevant back/fwd arrows

        //buildView();

        if (mViewModel.getRecipe() != null) {
            Log.d(TAG, "onCreate: Recipe selected:[" + mViewModel.getRecipe().getName() + "]");
            getSupportActionBar().setTitle(mViewModel.getRecipe().getName());

            if (savedInstanceState == null)
            //    onStepClick(mViewModel.getRecipe().getSteps().get(0)); // default selection
                mViewModel.setStepActive(mViewModel.getRecipe().getSteps().get(0));

            populatePreliminaryFields(mViewModel.getRecipe());
            
            
        } else {
            // TODO: 25/04/2018 Display error
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if ((!mIsTwoPane) && (mFragmentInstructions != null && mFragmentInstructions.isVisible())){
                    //mFragmentManager.beginTransaction().remove(mFragmentInstructions).commit();
                    applyFragment(mFragmentSteps, R.id.layout_main_steps, FRAGMENT_STEPS_TAG);
                    //((RecipeStepsFragment)mFragmentSteps).highlightActiveStep(mViewModel.getStepActive().getId());
                    //NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                //return true;
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);

        try {
            if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_STEPS_TAG) != null)
                getSupportFragmentManager().putFragment(outState, FRAGMENT_STEPS, mFragmentSteps);

            if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_INSTRUCTION_TAG) != null)
                getSupportFragmentManager().putFragment(outState, FRAGMENT_INSTRUCTION, mFragmentInstructions);
        }
        catch (Exception exc){
            Log.e(TAG, "onSaveInstanceState: EXCEPTION:["+exc.getLocalizedMessage()+"]" );
        }
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

    private void updateWidgetWithBroadcast(){
        Log.d(TAG, "updateWidgetWithBroadcast: ");
        Intent intent = new Intent(this, BakingWidgetProvider.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        sendBroadcast(intent);
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

    private void highlightStepOnVisibleFragmentAtPosition(int position){

    }

    @Override
    public void onFragmentStepNav_Back() {
        //int current_pos = ((RecipeStepsFragment)mFragmentSteps).getStepsAdapter().getSelectedPosition();
        int current_pos = mViewModel.getStepActive().getId();
        Log.d(TAG, "onFragmentStepNav_Back: current pos:"+String.valueOf(current_pos));
        //if ((current_pos > 0) && (mFragmentSteps.isVisible()))
        if (current_pos > 0) {
            int new_pos = current_pos-1;
            mViewModel.setStepActive(mViewModel.getRecipe().getSteps().get(new_pos));
            if (mIsTwoPane) {
                //if (mFragmentSteps.isVisible())
                ((RecipeStepsFragment) mFragmentSteps).highlightActiveStep(new_pos);
            }
            else {
                //((RecipeInstructionFragment) mFragmentInstructions).updateInstruction(current_pos - 1);
                ((RecipeInstructionFragment) mFragmentInstructions).updateInstruction(mViewModel.getStepActive());
            }
        }
        //mViewModel.setStepActive(mViewModel.getRecipe().getSteps().get(current_pos));
    }

    @Override
    public void onFragmentStepNav_Forward() {
        //int current_pos = ((RecipeStepsFragment)mFragmentSteps).getStepsAdapter().getSelectedPosition();
        int current_pos = mViewModel.getStepActive().getId();
        Log.d(TAG, "onFragmentStepNav_Forward: current pos:"+String.valueOf(current_pos));
//        if (current_pos < mViewModel.getRecipe().getSteps().size())
//            ((RecipeStepsFragment)mFragmentSteps).highlightActiveStep(current_pos+1);
//        mViewModel.setStepActive(mViewModel.getRecipe().getSteps().get(current_pos));

        if (current_pos < mViewModel.getRecipe().getSteps().size()){
            int new_pos = current_pos+1;
            mViewModel.setStepActive(mViewModel.getRecipe().getSteps().get(new_pos));
            if (mIsTwoPane)
                ((RecipeStepsFragment)mFragmentSteps).highlightActiveStep(new_pos);
            else
                ((RecipeInstructionFragment) mFragmentInstructions).updateInstruction(mViewModel.getStepActive());
        }

        //mViewModel.setStepActive(mViewModel.getRecipe().getSteps().get(current_pos));

    }

    @Override
    public void onStepClick(Step step) {
        Log.d(TAG, "onStepClick: step:["+String.valueOf(step.getId())+"]["+step.getShortDescription()+"]");
        mViewModel.setStepActive(step);

        if (!mIsTwoPane){
            // on phone, show instructions fragment if we are on Steps fragment
            //if (mFragmentSteps!=null && mFragmentSteps.isVisible()) {
                applyFragment(mFragmentInstructions, R.id.layout_main_steps, FRAGMENT_INSTRUCTION_TAG);
            //}
        }
        //else
            updateInstructionPane(step);
    }

    private void applyFragment(Fragment frag, int layoutId, String fragTag){
        if (frag != null)
            mFragmentManager.beginTransaction().replace(layoutId, frag, fragTag).commit();
        else{
            //mFragmentSteps = RecipeStepsFragment.newInstance(mViewModel.getRecipe());
            //mFragmentInstructions = RecipeInstructionFragment.newInstance(mViewModel.getRecipe());
            switch (fragTag){
                case FRAGMENT_STEPS_TAG:
                    frag = mFragmentSteps = RecipeStepsFragment.newInstance(mViewModel.getRecipe());
                    break;
                case FRAGMENT_INSTRUCTION_TAG:
                    frag = mFragmentInstructions = RecipeInstructionFragment.newInstance(mViewModel.getRecipe());
                    break;
            }
            mFragmentManager.beginTransaction()
                    .add(layoutId, frag, fragTag)
                    .commit();
        }
    }

    private void updateInstructionPane(Step step){
        //if (mIsTwoPane){
            //if (mFragmentManager.findFragmentByTag(FRAGMENT_INSTRUCTION_TAG) != null) {
        if (mFragmentInstructions!=null && mFragmentInstructions.isVisible())  {
                ((RecipeInstructionFragment)mFragmentInstructions).updateInstruction(step);
            }
        //}
        //else{
            // TODO: 25/04/2018
            // new activitry?
       // }
    }
}
