package com.bf.bakingapp;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import com.bf.bakingapp.ui.activity.MainActivity;
import com.bf.bakingapp.ui.activity.RecipeActivity;

@RunWith(AndroidJUnit4.class)
public class MainActivityToRecipeActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource()
    {
        IdlingResource mIdlingResource = mMainActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void showRecipeActivityForAnySelection(){
        Intents.init();

        onView(ViewMatchers.withId(R.id.recyclerview_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        intended(hasComponent(RecipeActivity.class.getName()));

        Intents.release();
    }

}


