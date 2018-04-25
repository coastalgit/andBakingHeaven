package com.bf.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bf.bakingapp.R;
import com.bf.bakingapp.model.Ingredient;

import java.util.ArrayList;
import java.util.List;


/*
 * @author frielb 
 * Created on 25/04/2018
 */

public class IngredientsAdapter extends ArrayAdapter<Ingredient> {

    private Context mContext;
    private ArrayList<Ingredient> mIngredients;

    public IngredientsAdapter(@NonNull Context context, @NonNull ArrayList<Ingredient> ingredients) {
        super(context, 0, ingredients);
        this.mContext = context;
        this.mIngredients = ingredients;
    }

    @Override
    public int getCount() {
        if (mIngredients == null)
            return 0;
        return mIngredients.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View ingredItem = convertView;
        ingredItem = LayoutInflater.from(mContext).inflate(R.layout.list_item_ingredient,parent,false);

        Ingredient ingredient = mIngredients.get(position);

        TextView tvMeasure = ingredItem.findViewById(R.id.tv_ingredient_measure);
        TextView tvDesc = ingredItem.findViewById(R.id.tv_ingredient_text);

        String measureString = ingredient.getQuantity() + " ("+ingredient.getMeasure() + ")";
        tvMeasure.setText(measureString);

        tvDesc.setText(ingredient.getIngredient());
        return ingredItem;
    }
}
