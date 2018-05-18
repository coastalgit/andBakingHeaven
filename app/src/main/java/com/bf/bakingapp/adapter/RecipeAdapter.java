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

import static com.bf.bakingapp.common.Constants.Fonts.FONT_INDIEFLOWER;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipesAdapterViewHolder> {

    private final Context mContext;
    private ArrayList<Recipe> mRecipes;
    private RecipeAdapterOnClickHandler mListener;

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    public RecipeAdapter(Context mContext, RecipeAdapterOnClickHandler listener ) {
        this.mContext = mContext;
        this.mListener = listener;
    }

    public void reloadAdapter(ArrayList<Recipe> recipes){
        this.mRecipes = recipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.list_item_recipe;

        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);

        return new RecipesAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecipesAdapterViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        holder.recipeName.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null)
            return 0;
        return mRecipes.size();
    }

    class RecipesAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView recipeName;

        public RecipesAdapterViewHolder(View itemView) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.tv_recipeitem_name);

            //Typeface font = Typeface.createFromAsset(mContext.getAssets(), FONT_TITILLIUM_REGULAR);
            Typeface font = Typeface.createFromAsset(mContext.getAssets(), FONT_INDIEFLOWER);
            recipeName.setTypeface(font);

            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                Recipe recipe = mRecipes.get(pos);
                mListener.onClick(recipe);
            });
        }


    }
}
