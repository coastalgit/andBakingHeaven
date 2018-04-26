package com.bf.bakingapp.viewmodel;

/*
 * @author frielb 
 * Created on 25/04/2018
 */

import android.arch.lifecycle.ViewModel;

import com.bf.bakingapp.model.Recipe;
import com.bf.bakingapp.model.Step;

public class ViewModelRecipe  extends ViewModel{

    private Recipe mRecipe;
    private Step mStepActive;

    public Recipe getRecipe() {
        return mRecipe;
    }

    public void setRecipe(Recipe mRecipe) {
        this.mRecipe = mRecipe;
    }

    public Step getStepActive() {
        return mStepActive;
    }

    public void setStepActive(Step mStepActive) {
        this.mStepActive = mStepActive;
    }

}
