package com.bf.bakingapp.viewmodel;

/*
 * @author frielb 
 * Created on 22/04/2018
 */

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bf.bakingapp.common.Enums;
import com.bf.bakingapp.model.Recipe;
import com.bf.bakingapp.worker.RecipeWorker;

import java.util.ArrayList;
import java.util.List;

public class ViewModelMain extends AndroidViewModel {

    private static final String TAG = ViewModelMain.class.getSimpleName();

    private RecipeWorker mWorker;
    private MutableLiveData<ArrayList<Recipe>> mRecipesObservable = new MutableLiveData<>();

    public ViewModelMain(@NonNull Application application) {
        super(application);
        getRecipesFromServer();
    }

    public void getRecipesFromServer(){
        if (mWorker == null)
            mWorker = new RecipeWorker(new RecipeWorker.IWorkerOnResponseHandler() {
                @Override
                public void onResponse_OK(ArrayList<Recipe> recipes) {
                    Log.d(TAG, "onResponse_OK: ");
                    mRecipesObservable.setValue(recipes);
                }

                @Override
                public void onResponse_Fail(Enums.ApiErrorCode errorCode, String errorMessage) {
                    Log.d(TAG, "onResponse_Fail: ");
                    mRecipesObservable = null;
                }
            });

        mWorker.getRecipesFromServer();
    }

    public MutableLiveData<ArrayList<Recipe>> getRecipesObservable() {
        if (mRecipesObservable == null) {
            mRecipesObservable = new MutableLiveData<ArrayList<Recipe>>();
            //getRecipesFromServer();
        }
        return mRecipesObservable;
    }

}
