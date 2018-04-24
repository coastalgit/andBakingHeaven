package com.bf.bakingapp.client;

/*
 * @author frielb 
 * Created on 24/04/2018
 *
 * http://square.github.io/retrofit/
 *
 */

import com.bf.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IRestClient {

    //http://go.udacity.com/android-baking-app-json
    //"https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    @GET("android-baking-app-json")
    Call<List<Recipe>> getRecipes();

}
