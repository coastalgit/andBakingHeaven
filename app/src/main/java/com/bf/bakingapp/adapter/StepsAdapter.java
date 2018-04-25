package com.bf.bakingapp.adapter;

/*
 * @author frielb 
 * Created on 24/04/2018
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bf.bakingapp.R;
import com.bf.bakingapp.model.Recipe;
import com.bf.bakingapp.model.Step;

import java.util.ArrayList;

import static com.bf.bakingapp.common.Constants.Fonts.FONT_INDIEFLOWER;
import static com.bf.bakingapp.common.Constants.Fonts.FONT_TITILLIUM_REGULAR;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.RecipesAdapterViewHolder> {

    private final Context mContext;
    private ArrayList<Step> mSteps;
    StepsAdapterOnClickHandler mListener;

    public interface StepsAdapterOnClickHandler {
        void onClick(Step step);
    }

    public StepsAdapter(Context mContext, StepsAdapterOnClickHandler listener ) {
        this.mContext = mContext;
        this.mListener = listener;
    }

    public void reloadAdapter(ArrayList<Step> steps){
        this.mSteps = steps;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.list_item_step;

        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);

        return new RecipesAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecipesAdapterViewHolder holder, int position) {
        Step step = mSteps.get(position);
        holder.recipeDescShort.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (mSteps == null)
            return 0;
        return mSteps.size();
    }

    class RecipesAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView recipeDescShort;

        public RecipesAdapterViewHolder(View itemView) {
            super(itemView);

            recipeDescShort = itemView.findViewById(R.id.tv_stepitem_desc_short);

            Typeface font = Typeface.createFromAsset(mContext.getAssets(), FONT_TITILLIUM_REGULAR);
            //Typeface font = Typeface.createFromAsset(mContext.getAssets(), FONT_INDIEFLOWER);
            recipeDescShort.setTypeface(font);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Step step = mSteps.get(pos);
                    mListener.onClick(step);
                }
            });
        }


    }
}
