package com.bf.bakingapp.manager;

/*
 * @author frielb 
 * Created on 01/05/2018
 *
 * Singleton class to hold persistent widget information
 */

import com.bf.bakingapp.model.Recipe;

public class RecipeManager {

    //private static final String TAG = RecipeManager.class.getSimpleName();

    private Recipe mRecipe;

    private static RecipeManager instance;

    public static RecipeManager getInstance()
    {
        if (instance == null)
        {
            synchronized (RecipeManager.class)
            {
                if (instance == null)
                {
                    instance = new RecipeManager();
                }
            }
        }
        return instance;
    }

    public Recipe getRecipe() {
        return mRecipe;
    }

    public void setRecipe(Recipe mRecipe) {
        this.mRecipe = mRecipe;
    }

}
