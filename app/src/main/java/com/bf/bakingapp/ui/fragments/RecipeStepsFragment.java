package com.bf.bakingapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bf.bakingapp.R;
import com.bf.bakingapp.adapter.StepsAdapter;
import com.bf.bakingapp.model.Recipe;
import com.bf.bakingapp.model.Step;
import com.bf.bakingapp.ui.activity.IngredientsActivity;
import com.bf.bakingapp.ui.activity.RecipeActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeStepsFragment extends Fragment implements StepsAdapter.StepsAdapterOnClickHandler {

    private static final String TAG = RecipeStepsFragment.class.getSimpleName();
    private final static String KEY_RECIPE = "key_recipe";

    public interface OnStepsFragmentInteractionListener {
        void onStepClick(Step step);
    }

    private Recipe mRecipe;
//    public StepsAdapter getStepsAdapter() {
//        return mStepsAdapter;
//    }
    private StepsAdapter mStepsAdapter;
    private boolean mIsLandscape = false;
    private boolean mIsTwoPane = false;

    @BindView(R.id.recyclerview_steps)
    RecyclerView mRecyclerViewSteps;

//    @BindView(R.id.tv_steps_title)
//    TextView mTvStepsTitle;

    @BindView(R.id.btn_steps_showingredients)
    Button mBtnShowIngredients;

    private OnStepsFragmentInteractionListener mListener;

    public RecipeStepsFragment() {
        // Required empty public constructor
    }

    public static RecipeStepsFragment newInstance(Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_RECIPE, recipe);
        RecipeStepsFragment fragment = new RecipeStepsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            mRecipe = (Recipe) bundle.getSerializable(KEY_RECIPE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: (Steps)");
        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        ButterKnife.bind(this, rootView);

        mIsTwoPane = getResources().getBoolean(R.bool.is_tabletsize);
        mIsLandscape = getResources().getBoolean(R.bool.is_landscape);

        readBundle(getArguments());

        if (savedInstanceState == null) {
            Log.d(TAG, "onCreateView: NO  INSTANCE");
        }
        else{
            Log.d(TAG, "onCreateView: HAVE INSTANCE");
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerViewSteps.setLayoutManager(linearLayoutManager);
        mRecyclerViewSteps.setHasFixedSize(true);
        mStepsAdapter = new StepsAdapter(getActivity(),this);
        mRecyclerViewSteps.setAdapter(mStepsAdapter);

        mStepsAdapter.reloadAdapter(new ArrayList<Step>(mRecipe.getSteps()));

        Log.d(TAG, "onCreateView: active step="+String.valueOf(((RecipeActivity)getActivity()).getViewModel().getStepActive().getId()));
        Step activeStep  = ((RecipeActivity)getActivity()).getViewModel().getStepActive();
        if (activeStep != null)
            highlightActiveStep(((RecipeActivity)getActivity()).getViewModel().getStepActive().getId());
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    public void highlightActiveStep(int pos){
        Log.d(TAG, "highlightActiveStep: pos="+String.valueOf(pos));
        if (mRecyclerViewSteps.findViewHolderForAdapterPosition(pos) != null)
            mRecyclerViewSteps.findViewHolderForAdapterPosition(pos).itemView.performClick();
        mStepsAdapter.setSelectedPosition(pos);
        //mRecyclerViewSteps.scrollToPosition(pos);
        //mRecyclerViewSteps.smoothScrollToPosition(pos);
        //mRecyclerViewSteps.getLayoutManager().smoothScrollToPosition(mRecyclerViewSteps, null, pos);
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mRecyclerViewSteps.getLayoutManager().scrollToPosition(pos);
//            }
//        });

    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: (Steps)");
        super.onAttach(context);
        if (context instanceof OnStepsFragmentInteractionListener) {
            mListener = (OnStepsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStepsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void showIngredients(){
        Intent intent = new Intent(getActivity(), IngredientsActivity.class);
        intent.putExtra(IngredientsActivity.KEY_RECIPE, mRecipe );
        startActivity(intent);
    }
    @Override
    public void onClick(Step step) {
        Log.d(TAG, "onClick: Step:["+String.valueOf(step.getId())+"]");
        if (mListener != null) {
            mListener.onStepClick(step);
        }
    }

    @OnClick(R.id.btn_steps_showingredients)
    public void btnShowIngredients_onClick(Button btn){
        showIngredients();
    }


}
