package com.bf.bakingapp.viewmodel;

/*
 * @author frielb 
 * Created on 25/04/2018
 */

import android.arch.lifecycle.ViewModel;

import com.bf.bakingapp.model.Recipe;

public class ViewModelRecipe  extends ViewModel{

    private Recipe mRecipe;

    public Recipe getRecipe() {
        return mRecipe;
    }

    public void setRecipe(Recipe mRecipe) {
        this.mRecipe = mRecipe;
    }




}
