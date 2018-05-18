package com.bf.bakingapp.worker;

/*
 * @author frielb 
 * Created on 24/04/2018
 */

import android.support.annotation.NonNull;
import android.util.Log;

import com.bf.bakingapp.client.IRestClient;
import com.bf.bakingapp.common.Constants;
import com.bf.bakingapp.common.Enums;
import com.bf.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeWorker {

    private static final String TAG = RecipeWorker.class.getSimpleName();

    @SuppressWarnings("unused")
    public interface IWorkerOnResponseHandler{
        void onResponse_OK(ArrayList<Recipe> recipes);
        void onResponse_Fail(Enums.ApiErrorCode errorCode, String errorMessage);
    }

    private IWorkerOnResponseHandler mListener;
    private IRestClient mRestClient = null;

    public RecipeWorker(IWorkerOnResponseHandler listener) {
        this.mListener = listener;
        createClient();
    }

    public void getRecipesFromServer(){
        Call<List<Recipe>> call = mRestClient.getRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                if (response.isSuccessful()){
                    ArrayList<Recipe> recipeList = (ArrayList<Recipe>) response.body();
                    if (recipeList != null){
                        int recipeCount = recipeList.size();
                        Log.d(TAG, "onResponse: Recipe count:"+String.valueOf(recipeCount));
                        mListener.onResponse_OK(recipeList);
                    }
                    else{
                        mListener.onResponse_Fail(Enums.ApiErrorCode.INVALID_DATA, "No recipes available on server");
                    }
                }
                else{
                    Log.d(TAG, "onResponse: ");
                    mListener.onResponse_Fail(Enums.ApiErrorCode.INVALID_RESPONSE, "Recipes response error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: ");
                mListener.onResponse_Fail(Enums.ApiErrorCode.CONNECTION_ERROR, "Recipes connection error");
            }
        });
    }

    private void createClient(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        mRestClient = retrofit.create(IRestClient.class);
    }

}
