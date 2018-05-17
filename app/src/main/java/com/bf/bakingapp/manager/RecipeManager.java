package com.bf.bakingapp.manager;

/*
 * @author frielb 
 * Created on 01/05/2018
 *
 * Singleton class to hold persistent widget information
 */

import android.content.Context;
import android.util.Log;

import com.bf.bakingapp.model.Ingredient;
import com.bf.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeManager {

    private static final String TAG = RecipeManager.class.getSimpleName();

    // TODO: Later. I/O Allow for persistent info to be written to device storage (allowing widget update on app start)

    private Context mContext;
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
