package com.bf.bakingapp.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bf.bakingapp.R;
import com.bf.bakingapp.common.Constants;
import com.bf.bakingapp.model.Recipe;
import com.bf.bakingapp.model.Step;
import com.bf.bakingapp.ui.activity.RecipeActivity;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeInstructionFragment extends Fragment{

    private static final String TAG = RecipeInstructionFragment.class.getSimpleName();
    private final static String KEY_RECIPE = "key_recipe";
    private final static String KEY_PLAYBACKPOSITION = "key_playpos";

    public interface OnInstructionFragmentInteractionListener {
        void onFragmentStepNav_Back();
        void onFragmentStepNav_Forward();
    }

    private Recipe mRecipe;
    private Step mStepActive;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mExoPlayerView;
    private long mPlaybackPosition;

    @BindView(R.id.tv_instruction_title)
    TextView mTvInstructionTitle;
    @BindView(R.id.tv_instruction_fulldesc)
    TextView mTvInstructionFullDesc;
    @BindView(R.id.tv_message_novideo)
    TextView mTvMessageNoVideo;

    @BindView(R.id.btn_instruct_navback)
    ImageButton mBtnNav_Back;
    @BindView(R.id.btn_instruct_navforward)
    ImageButton mBtnNav_Forward;

    private OnInstructionFragmentInteractionListener mListener;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);

        outState.putLong(KEY_PLAYBACKPOSITION, mPlaybackPosition);
    }

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
        setRetainInstance(true);

        View rootView = inflater.inflate(R.layout.fragment_recipe_instruction, container, false);
        ButterKnife.bind(this, rootView);

        readBundle(getArguments());

        if (savedInstanceState == null) {
            Log.d(TAG, "onCreateView: NO  INSTANCE");
        }
        else{
            Log.d(TAG, "onCreateView: HAVE INSTANCE. Restored Playback pos:"+String.valueOf(mPlaybackPosition));
            setPlaybackPosition(savedInstanceState.getLong(KEY_PLAYBACKPOSITION, 0));
        }

        mExoPlayerView = rootView.findViewById(R.id.exoPlayerView);

        releasePlayer();
        buildView();

        return rootView;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
        if (Util.SDK_INT > 23){
            Log.d(TAG, "onCreateView: active step="+String.valueOf(((RecipeActivity)getActivity()).getViewModel().getStepActive().getId()));
            updateInstruction(((RecipeActivity)getActivity()).getViewModel().getStepActive());
        }
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();

        if (Util.SDK_INT > 23){
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();

        if (mExoPlayer != null)
            setPlaybackPosition(mExoPlayer.getCurrentPosition());

        if (Util.SDK_INT <= 23)
            releasePlayer();

    }

    private void buildView(){
        if (mRecipe != null){
            mTvInstructionTitle.setText(mRecipe.getName());
            return;
        }
        Log.d(TAG, "buildView: Recipe is NULL");
    }

    private void setPlaybackPosition(long pos){
        Log.d(TAG, "setPlaybackPosition: set to :"+ String.valueOf(pos));
        mPlaybackPosition = pos;
    }

    public void updateInstruction(Step step){
        if (step != null){

            String ns = mStepActive==null?"null":String.valueOf(mStepActive.getId());
            Log.d(TAG, "updateInstruction: Previous Step["+String.valueOf(step.getId())+"] New Step ["+ns+"]");

            if (mStepActive != null){
                if (step.getId() != mStepActive.getId()) {
                    releasePlayer();
                    setPlaybackPosition(0);
                }
            }

            mStepActive = step;
            Log.d(TAG, "updateInstruction: select["+step.getShortDescription()+"] at pos["+String.valueOf(step.getId())+"]");

            mTvInstructionFullDesc.setText(step.getDescription());

            int stepCountInRecipe = mRecipe.getSteps().size();
            int stepIdToDisplay = step.getId();
            Log.d(TAG, "updateInstruction: recipe count="+String.valueOf(stepCountInRecipe)+" step id="+String.valueOf(stepIdToDisplay)+")");
            mBtnNav_Back.setVisibility(stepIdToDisplay==0?View.INVISIBLE:View.VISIBLE);
            mBtnNav_Forward.setVisibility(stepIdToDisplay==(stepCountInRecipe-1)?View.INVISIBLE:View.VISIBLE);

            String urlString = step.getThumbnailURL();
            if (TextUtils.isEmpty(urlString))
                urlString = step.getVideoURL();
            setVideoDisplay(urlString);
        }
    }

    private void initializeExoPlayer(String videoUrl){
        if (mExoPlayer == null && !(TextUtils.isEmpty(videoUrl))){

            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(); // bandwidth during playback
            RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
            mExoPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(getContext(), Constants.APP_NAME);
            Uri videoUri = Uri.parse(videoUrl);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent, bandwidthMeter);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(videoUri);
            mExoPlayer.prepare(mediaSource);

            Log.d(TAG, "initializeExoPlayer: seek to:"+String.valueOf(mPlaybackPosition));
            mExoPlayer.seekTo(mPlaybackPosition);

            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void setVideoDisplay(String videoUrl){
        if (TextUtils.isEmpty(videoUrl)){
            mTvInstructionTitle.setText("NO VIDEO");
            mTvMessageNoVideo.setVisibility(View.VISIBLE);
            mExoPlayerView.setVisibility(View.GONE);
        }
        else{
            mTvInstructionTitle.setText(videoUrl);
            mTvMessageNoVideo.setVisibility(View.INVISIBLE);
            mExoPlayerView.setVisibility(View.VISIBLE);

            initializeExoPlayer(videoUrl);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: (Instruction)");
        super.onAttach(context);
        if (context instanceof OnInstructionFragmentInteractionListener) {
            mListener = (OnInstructionFragmentInteractionListener) context;
        }
    }


    @OnClick(R.id.btn_instruct_navback)
    public void btnNavBack_onClick(ImageButton btn){
        if (mExoPlayer != null)
            mExoPlayer.stop();

        if (mListener != null)
            mListener.onFragmentStepNav_Back();
    }

    @OnClick(R.id.btn_instruct_navforward)
    public void btnNavForward_onClick(ImageButton btn){
        if (mExoPlayer != null)
            mExoPlayer.stop();

        if (mListener != null)
            mListener.onFragmentStepNav_Forward();
    }


}
