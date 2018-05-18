package com.bf.bakingapp.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.bf.bakingapp.R;
import com.bf.bakingapp.manager.RecipeManager;
import com.bf.bakingapp.model.Recipe;
import com.bf.bakingapp.model.Step;
import com.bf.bakingapp.ui.fragments.RecipeInstructionFragment;
import com.bf.bakingapp.ui.fragments.RecipeStepsFragment;
import com.bf.bakingapp.viewmodel.ViewModelRecipe;
import com.bf.bakingapp.widget.BakingWidgetProvider;

@SuppressWarnings("ConstantConditions")
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

    private ViewModelRecipe mViewModel;

    //private boolean mIsLandscape = false;
    private boolean mIsTwoPane = false;

    private FragmentManager mFragmentManager;
    private Fragment mFragmentSteps;
    private Fragment mFragmentInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_recipe);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mViewModel = ViewModelProviders.of(this).get(ViewModelRecipe.class);
        if (mViewModel.getRecipe() == null){
            if (getIntent().hasExtra(KEY_RECIPE)) {
                mViewModel.setRecipe((Recipe)getIntent().getSerializableExtra(KEY_RECIPE));
                Log.d(TAG, "onCreate: Has INTENT. Recipe is "+mViewModel.getRecipe()==null?"NULL":"NOT NULL");
            }
        }
        else
            Log.d(TAG, "onCreate: VM has recipe");

        updateWidgetStorageWithIngredients(mViewModel.getRecipe());

        mIsTwoPane = getResources().getBoolean(R.bool.is_tabletsize);
        //mIsLandscape = getResources().getBoolean(R.bool.is_landscape);

        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            Log.d(TAG, "onCreate: NO INSTANCE");
            applyFragment(mFragmentSteps, R.id.layout_main_steps, FRAGMENT_STEPS_TAG);

            mFragmentInstructions = RecipeInstructionFragment.newInstance(mViewModel.getRecipe());
            if (mIsTwoPane) {
                applyFragment(mFragmentInstructions, R.id.layout_main_instructions, FRAGMENT_INSTRUCTION_TAG);
            }
        }
        else{
            Log.d(TAG, "onCreate: HAVE INSTANCE");
            mFragmentSteps = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_STEPS);
            mFragmentInstructions = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_INSTRUCTION);
        }

        updateWidgetWithBroadcast();

        if (mViewModel.getRecipe() != null) {
            Log.d(TAG, "onCreate: Recipe selected:[" + mViewModel.getRecipe().getName() + "]");
            getSupportActionBar().setTitle(mViewModel.getRecipe().getName());

            if (savedInstanceState == null)
                mViewModel.setStepActive(mViewModel.getRecipe().getSteps().get(0));

            //populatePreliminaryFields(mViewModel.getRecipe());
            
            
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if ((!mIsTwoPane) && (mFragmentInstructions != null && mFragmentInstructions.isVisible())){
                    applyFragment(mFragmentSteps, R.id.layout_main_steps, FRAGMENT_STEPS_TAG);
                    return true;
                }
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

    private void updateWidgetStorageWithIngredients(Recipe recipe){
        RecipeManager.getInstance().setRecipe(recipe);
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

    @Override
    public void onFragmentStepNav_Back() {
        int current_pos = mViewModel.getStepActive().getId();
        Log.d(TAG, "onFragmentStepNav_Back: current pos:"+String.valueOf(current_pos));

        if (current_pos > 0) {
            int new_pos = current_pos-1;
            mViewModel.setStepActive(mViewModel.getRecipe().getSteps().get(new_pos));

            if (mIsTwoPane)
                ((RecipeStepsFragment) mFragmentSteps).highlightActiveStep(new_pos);
            else
                ((RecipeInstructionFragment) mFragmentInstructions).updateInstruction(mViewModel.getStepActive());
        }
    }

    @Override
    public void onFragmentStepNav_Forward() {
        int current_pos = mViewModel.getStepActive().getId();
        Log.d(TAG, "onFragmentStepNav_Forward: current pos:"+String.valueOf(current_pos));

        if (current_pos < mViewModel.getRecipe().getSteps().size()){
            int new_pos = current_pos+1;
            mViewModel.setStepActive(mViewModel.getRecipe().getSteps().get(new_pos));
            if (mIsTwoPane)
                ((RecipeStepsFragment)mFragmentSteps).highlightActiveStep(new_pos);
            else
                ((RecipeInstructionFragment) mFragmentInstructions).updateInstruction(mViewModel.getStepActive());
        }
    }

    @Override
    public void onStepClick(Step step) {
        Log.d(TAG, "onStepClick: step:["+String.valueOf(step.getId())+"]["+step.getShortDescription()+"]");
        mViewModel.setStepActive(step);

        if (!mIsTwoPane)
            applyFragment(mFragmentInstructions, R.id.layout_main_steps, FRAGMENT_INSTRUCTION_TAG);

        updateInstructionPane(step);
    }

    private void applyFragment(Fragment frag, int layoutId, String fragTag){
        if (frag != null)
            mFragmentManager.beginTransaction().replace(layoutId, frag, fragTag).commit();
        else{
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
        if (mFragmentInstructions!=null && mFragmentInstructions.isVisible())
           ((RecipeInstructionFragment)mFragmentInstructions).updateInstruction(step);
    }
}
