package com.bf.bakingapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

import com.bf.bakingapp.ui.activity.MainActivity;

@RunWith(AndroidJUnit4.class)
public class MainActivityToRecipeActivityTest {

    private IdlingResource mIdlingResource;
    private static String recipeName = "Brownies";


    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource()
    {
        mIdlingResource = mMainActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void showRecipeActivityForSelection(){
        onView(ViewMatchers.withId(R.id.recyclerview_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        //onView(withId(R.)
        onView(allOf(isDescendantOfA(withResourceName("android:id/action_bar_container")), withText(recipeName))).check(matches(isDisplayed()));
    }


}


