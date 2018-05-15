package com.bf.bakingapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.bf.bakingapp.ui.activity.MainActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityToRecipeActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void showRecipeActivityForSelection(){
        onView(ViewMatchers.withId(R.id.recyclerview_recipes))
                .perform(RecyclerViewActions.action)
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.bf.bakingapp", appContext.getPackageName());
    }

}


