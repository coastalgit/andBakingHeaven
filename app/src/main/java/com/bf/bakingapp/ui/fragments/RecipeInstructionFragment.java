package com.bf.bakingapp.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bf.bakingapp.R;
import com.bf.bakingapp.adapter.StepsAdapter;
import com.bf.bakingapp.model.Recipe;
import com.bf.bakingapp.model.Step;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeInstructionFragment extends Fragment{


    private static final String TAG = RecipeInstructionFragment.class.getSimpleName();
    private final static String KEY_RECIPE = "key_recipe";

    public interface OnInstructionFragmentInteractionListener {
        void onFragmentStepNav_Back();
        void onFragmentStepNav_Forward();
    }

    private Recipe mRecipe;

    @BindView(R.id.tv_instruction_title)
    TextView mTvInstructionTitle;
    @BindView(R.id.tv_instruction_fulldesc)
    TextView mTvInstructionFullDesc;

    @BindView(R.id.btn_instruct_navback)
    ImageButton mBtnNav_Back;
    @BindView(R.id.btn_instruct_navforward)
    ImageButton mBtnNav_Forward;

    private OnInstructionFragmentInteractionListener mListener;

    public RecipeInstructionFragment() {
        // Required empty public constructor
    }

    public static RecipeInstructionFragment newInstance(Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_RECIPE, recipe);
        RecipeInstructionFragment fragment = new RecipeInstructionFragment();
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
        Log.d(TAG, "onCreateView: (Instruction)");
        View rootView = inflater.inflate(R.layout.fragment_recipe_instruction, container, false);
        ButterKnife.bind(this, rootView);

        readBundle(getArguments());

        if (savedInstanceState == null) {
            Log.d(TAG, "onCreateView: NO  INSTANCE");
        }
        else{
            Log.d(TAG, "onCreateView: HAVE INSTANCE");
        }

        buildView();

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

    private void buildView(){
        if (mRecipe != null){
            mTvInstructionTitle.setText(mRecipe.getName());
            return;
        }
        Log.d(TAG, "buildView: Recipe is NULL");
    }

/*
    public void updateInstruction(int stepId){
        if (mRecipe != null){
            Step step = mRecipe.getSteps().get(stepId);
            Log.d(TAG, "updateInstruction: select["+step.getShortDescription()+"] at pos["+String.valueOf(stepId)+"]");
            mTvInstructionTitle.setText(step.getVideoURL());
            mTvInstructionFullDesc.setText(step.getDescription());
        }
    }
*/

    public void updateInstruction(Step step){
        if (step != null){
            //Step step = mRecipe.getSteps().get(stepId);
            Log.d(TAG, "updateInstruction: select["+step.getShortDescription()+"] at pos["+String.valueOf(step.getId())+"]");
            mTvInstructionTitle.setText(step.getVideoURL());
            mTvInstructionFullDesc.setText(step.getDescription());

            int stepCountInRecipe = mRecipe.getSteps().size();
            int stepIdToDisplay = step.getId();
            Log.d(TAG, "updateInstruction: recipe count="+String.valueOf(stepCountInRecipe)+" step id="+String.valueOf(stepIdToDisplay)+")");
            mBtnNav_Back.setVisibility(stepIdToDisplay==0?View.INVISIBLE:View.VISIBLE);
            mBtnNav_Forward.setVisibility(stepIdToDisplay==(stepCountInRecipe-1)?View.INVISIBLE:View.VISIBLE);

        }
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: (Instruction)");
        super.onAttach(context);
        if (context instanceof OnInstructionFragmentInteractionListener) {
            mListener = (OnInstructionFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnInstructionFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.btn_instruct_navback)
    public void btnNavBack_onClick(ImageButton btn){
        if (mListener != null)
            mListener.onFragmentStepNav_Back();
    }

    @OnClick(R.id.btn_instruct_navforward)
    public void btnNavForward_onClick(ImageButton btn){
        if (mListener != null)
            mListener.onFragmentStepNav_Forward();
    }


}
